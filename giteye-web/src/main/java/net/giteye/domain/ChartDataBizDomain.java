package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.giteye.db.entity.ChartData;
import net.giteye.db.entity.ChartDataRecord;
import net.giteye.db.service.ChartDataRecordService;
import net.giteye.db.service.ChartDataService;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartDataVO;
import net.giteye.vo.ChartRecordVO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ChartDataBizDomain {

    @Resource
    private ChartDataService chartDataService;

    @Resource
    private ChartDataRecordService chartDataRecordService;

    public void batchSaveChartData(List<ChartDataVO> chartDataVOList){
        List<ChartData> dataList = new ArrayList<>();
        for (ChartDataVO item : chartDataVOList){
            dataList.add(BeanUtil.copyProperties(item, ChartData.class));
        }
        chartDataService.saveOrUpdateBatch(dataList, 500);
    }

    public List<ChartDataVO> getChartData(ChartDataRecordVO chartDataRecordVO){
        QueryWrapper<ChartData> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", chartDataRecordVO.getUserId()))
                .and(wrapper -> wrapper.eq("git_site", chartDataRecordVO.getGitSite()))
                .and(wrapper -> wrapper.eq("owner", chartDataRecordVO.getGitUsername()))
                .and(wrapper -> wrapper.eq("repo", chartDataRecordVO.getRepoName()))
                .and(wrapper -> wrapper.eq("metrics_type", chartDataRecordVO.getMetricsType()));
        queryWrapper.orderByAsc("id");

        List<ChartData> list = chartDataService.list(queryWrapper);

        List<ChartDataVO> resultList = new ArrayList<>();
        for (ChartData item : list){
            resultList.add(BeanUtil.copyProperties(item, ChartDataVO.class));
        }
        return resultList;
    }

    public void deleteChartData(Long chartDataRecordId){
        QueryWrapper<ChartData> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("data_record_id", chartDataRecordId));
        chartDataService.remove(queryWrapper);
    }

    public ChartDataRecordVO queryOrInsertChartDataRecord(ChartRecordVO chartRecordVO){
        QueryWrapper<ChartDataRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", chartRecordVO.getUserId()))
                .and(wrapper -> wrapper.eq("git_site", chartRecordVO.getGitSite()))
                .and(wrapper -> wrapper.eq("git_username", chartRecordVO.getGitUsername()))
                .and(wrapper -> wrapper.eq("repo_name", chartRecordVO.getRepoName()))
                .and(wrapper -> wrapper.eq("metrics_type", chartRecordVO.getMetricsType()));

        ChartDataRecord record = chartDataRecordService.getOne(queryWrapper, false);
        if (ObjectUtil.isNull(record)){
            record = new ChartDataRecord();
            record.setUserId(chartRecordVO.getUserId());
            record.setGitSite(chartRecordVO.getGitSite());
            record.setGitUsername(chartRecordVO.getGitUsername());
            record.setRepoName(chartRecordVO.getRepoName());
            record.setMetricsType(chartRecordVO.getMetricsType());
            record.setCreateTime(new Date());
            chartDataRecordService.save(record);
        }
        return BeanUtil.copyProperties(record, ChartDataRecordVO.class);
    }

    public void updateChartDataRecord(ChartDataRecordVO chartDataRecordVO){
        chartDataRecordService.updateById(BeanUtil.copyProperties(chartDataRecordVO, ChartDataRecord.class));
    }

    public ChartDataVO getMetricsLastChartData(ChartDataRecordVO chartDataRecordVO){
        //寻找上一个的lastYValue,如果有的话,则取上一个lastYValue
        QueryWrapper<ChartData> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("data_record_id", chartDataRecordVO.getId()))
                .and(wrapper -> wrapper.eq("user_id", chartDataRecordVO.getUserId()))
                .and(wrapper -> wrapper.eq("git_site", chartDataRecordVO.getGitSite()))
                .and(wrapper -> wrapper.eq("owner", chartDataRecordVO.getGitUsername()))
                .and(wrapper -> wrapper.eq("repo", chartDataRecordVO.getRepoName()))
                .and(wrapper -> wrapper.eq("metrics_type", chartDataRecordVO.getMetricsType()));
        queryWrapper.orderByDesc("id");
        ChartData lastChartData = chartDataService.getOne(queryWrapper, false);
        if (ObjectUtil.isNull(lastChartData)){
            return null;
        }
        return BeanUtil.copyProperties(lastChartData, ChartDataVO.class);
    }
}
