package net.giteye.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import net.giteye.domain.ChartDataBizDomain;
import net.giteye.domain.ChartImageBizDomain;
import net.giteye.domain.ChartRecordBizDomain;
import net.giteye.domain.GiteeBizDomain;
import net.giteye.enums.ChartRecordStatus;
import net.giteye.property.GeProperty;
import net.giteye.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public abstract class AbsChartTask implements ChartTask{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ChartRecordBizDomain chartRecordBizDomain;

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Resource
    private ChartImageBizDomain chartImageBizDomain;

    @Resource
    private ChartDataBizDomain chartDataBizDomain;

    @Resource
    private GeProperty geProperty;

    //生成一个图表
    public void generateOneChart(ChartDataRecordVO chartDataRecordVO, boolean refreshAllData){
        List<ChartRecordVO> chartRecordVOList = null;
        try{
            log.info("--------Repo [{}] task generate--------", chartDataRecordVO.getRepoName());

            //如果是全量更新，需要把最后页数和最后条数下标清空
            if (refreshAllData){
                chartDataRecordVO.setLastDataPage(null);
                chartDataRecordVO.setLastDataIndex(null);
            }

            //查出关联的ChartRecord
            chartRecordVOList = chartRecordBizDomain.queryChartRecordByChartDataRecord(chartDataRecordVO);
            if (CollectionUtil.isEmpty(chartRecordVOList)){
                return;
            }

            //把关联的ChartRecord改状态为正在生成
            for (ChartRecordVO chartRecordVO : chartRecordVOList){
                if (chartRecordVO.getStatus().equals(ChartRecordStatus.INIT.getCode())){
                    chartRecordVO.setStatus(ChartRecordStatus.GENERATING.getCode());
                }else{
                    chartRecordVO.setStatus(ChartRecordStatus.UPDATING.getCode());
                }
                chartRecordVO.setUpdateTime(new Date());
                chartRecordVO.setMsg(null);
                chartRecordBizDomain.updateChartRecord(chartRecordVO);
            }

            //获取到gitee的授权信息，这里因为是跑任务，并不都是由页面发起，所以不能从session里获取，要根据userId去查
//            GiteeUserAuthVO giteeUserAuthVO = giteeBizDomain.getUserAvailableUserAuth(chartDataRecordVO.getUserId());
            GiteeUserAuthVO giteeUserAuthVO = giteeBizDomain.getUserAvailableUserAuth(geProperty.getDefaultAuthUserId());

            //如果是全量更新的话，先把历史数据删掉
            if (refreshAllData){
                chartDataBizDomain.deleteChartData(chartDataRecordVO.getId());
            }

            //处理数据
            List<ChartDataVO> chartDataList = process(giteeUserAuthVO, chartDataRecordVO);

            //设置是否支持增量
            chartDataRecordVO.setSupportIncrement(isSupportIncrement());

            //如果是全量，肯定要生成图表，如果是增量，则要判断，结果集里面是不是有新插入的数据，
            //如果没有新插入，也就代表此次增量没有数据，则不需要生成图表，节省cpu开销
            boolean needGenerateChart = false;
            if (refreshAllData){
                needGenerateChart = true;
            }else{
                //判断是不是有新插入的
                for (ChartDataVO item : chartDataList){
                    //只要找到一个id为空的，就代表新插入的
                    if (ObjectUtil.isNull(item.getId())){
                        needGenerateChart = true;
                        break;
                    }else{
                        //如果有id不为空的，还要判断y_value是不是和数据库里一直，如果不一致，也要生成图表
                        ChartDataVO chartDataVO = chartDataBizDomain.getById(item.getId());
                        if (item.getyValue() > chartDataVO.getyValue()){
                            needGenerateChart = true;
                            break;
                        }
                    }
                }
            }

            //批量插入或者更新到数据库中
            chartDataBizDomain.batchSaveChartData(chartDataList);

            if (needGenerateChart){
                //循环关联的chartRecord,生成图片
                for (ChartRecordVO chartRecordItem : chartRecordVOList){
                    //生成图片
                    ChartImageGenerateResultVO imageGenerateResultVO = chartImageBizDomain.generateChartImage(chartRecordItem);

                    //更新chartRecord数据
                    if (imageGenerateResultVO.getSuccess()) {
                        chartRecordItem.setImgType(imageGenerateResultVO.getImgType());
                        chartRecordItem.setImgFileName(imageGenerateResultVO.getImgFileName());
                        chartRecordItem.setImgUrl(imageGenerateResultVO.getImgUrl());
                        chartRecordItem.setImgThumbUrl(imageGenerateResultVO.getThumbnailUrl());
                        chartRecordItem.setUploadConsume(DateUtil.betweenMs(imageGenerateResultVO.getUploadStartTime(), imageGenerateResultVO.getUploadEndTime()));
                        chartRecordItem.setGenerateConsume(DateUtil.betweenMs(imageGenerateResultVO.getGenerateStartTime(), imageGenerateResultVO.getGenerateEndTime()));
                        chartRecordItem.setMsg(imageGenerateResultVO.getMsg());
                        chartRecordItem.setStatus(ChartRecordStatus.UPLOADED.getCode());
                    } else {
                        chartRecordItem.setStatus(ChartRecordStatus.FAILED.getCode());
                        chartRecordItem.setMsg(imageGenerateResultVO.getMsg());
                    }
                    chartRecordItem.setUpdateTime(new Date());
                    chartRecordBizDomain.updateChartRecord(chartRecordItem);
                }
            }else{
                for (ChartRecordVO chartRecordVO : chartRecordVOList){
                    chartRecordVO.setStatus(ChartRecordStatus.UPLOADED.getCode());
                    chartRecordVO.setUpdateTime(new Date());
                    chartRecordVO.setMsg("success");
                    chartRecordBizDomain.updateChartRecord(chartRecordVO);
                }
            }

            //更新chartDataRecord
            chartDataBizDomain.updateChartDataRecord(chartDataRecordVO);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            for (ChartRecordVO chartRecordItem : chartRecordVOList){
                chartRecordItem.setStatus(ChartRecordStatus.FAILED.getCode());
                chartRecordItem.setMsg("系统错误");
                chartRecordItem.setUpdateTime(new Date());
                chartRecordBizDomain.updateChartRecord(chartRecordItem);
            }
        }
    }

    protected ChartDataVO initChartData(ChartDataRecordVO chartDataRecordVO){
        ChartDataVO chartDataVO = new ChartDataVO();
        chartDataVO.setDataRecordId(chartDataRecordVO.getId());
        chartDataVO.setUserId(chartDataRecordVO.getUserId());
        chartDataVO.setGitSite(chartDataRecordVO.getGitSite());
        chartDataVO.setOwner(chartDataRecordVO.getGitUsername());
        chartDataVO.setRepo(chartDataRecordVO.getRepoName());
        chartDataVO.setMetricsType(chartDataRecordVO.getMetricsType());
        chartDataVO.setCreateDate(new Date());
        return chartDataVO;
    }

    @Override
    public void check(ChartDataRecordVO chartDataRecordVO){

    }

    public List<ChartDataVO> frameInsert(List<ChartDataVO> chartDataList){
        return chartDataList;
    }

    protected abstract boolean isSupportIncrement();

    protected abstract List<ChartDataVO> process(GiteeUserAuthVO giteeUserAuthVO, ChartDataRecordVO chartDataRecordVO);
}
