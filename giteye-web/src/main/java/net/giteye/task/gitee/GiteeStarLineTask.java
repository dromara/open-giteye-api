package net.giteye.task.gitee;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.http.ForestResponse;
import net.giteye.client.gitee.GiteeRepoClient;
import net.giteye.enums.ChartMetrics;
import net.giteye.enums.GitSite;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.task.AbsChartTask;
import net.giteye.task.processor.StarLineProcessor;
import net.giteye.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 图表数据生成任务器
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
@Component
public class GiteeStarLineTask extends AbsChartTask {

    private static Logger log = LoggerFactory.getLogger(GiteeStarLineTask.class);

    @Resource
    private GiteeRepoClient giteeRepoClient;

    @Override
    protected List<ChartDataVO> process(GiteeUserAuthVO giteeUserAuthVO, ChartDataRecordVO chartDataRecordVO) {
        //获取到Gitee的star数据,并获取最终的chartData数据
        int page = ObjectUtil.isNull(chartDataRecordVO.getLastDataPage()) ? 1 : chartDataRecordVO.getLastDataPage();
        int pageSize = 50;
        int dataIndex = ObjectUtil.isNull(chartDataRecordVO.getLastDataIndex()) ? 0 : chartDataRecordVO.getLastDataIndex();
        List<StargazerInfoVO> starInfoList;
        List<StargazerInfoVO> subStarInfoList;
        StarLineProcessor starLineProcessor = StarLineProcessor.newInstance();
        while (true) {
            starInfoList = loadRepoStars(chartDataRecordVO, giteeUserAuthVO, page, pageSize);
            subStarInfoList = ListUtil.sub(starInfoList, dataIndex, starInfoList.size());

            //表示有新的
            if (CollectionUtil.isEmpty(subStarInfoList)) {
                dataIndex = starInfoList.size();
                break;
            }

            starLineProcessor.process(subStarInfoList, chartDataRecordVO);

            //这一页就是最后一页了
            if (starInfoList.size() != pageSize){
                dataIndex = starInfoList.size();
                break;
            }

            dataIndex = 0;
            page++;
        }
        //设置page和dataIndex字段
        chartDataRecordVO.setLastDataPage(page);
        chartDataRecordVO.setLastDataIndex(dataIndex);

        return starLineProcessor.getChartDataList();
    }

    private List<StargazerInfoVO> loadRepoStars(ChartDataRecordVO chartDataRecordVO,
                                                GiteeUserAuthVO giteeUserAuthVO,
                                                int page, int pageSize) {
        ForestResponse<List<StargazerInfoVO>> response = giteeRepoClient.getRepoStars(giteeUserAuthVO.getAccessToken(),
                chartDataRecordVO.getGitUsername(),
                chartDataRecordVO.getRepoName(),
                page,
                pageSize);
        if (response.isError()) {
            throw new GeException(GeErrorCode.CHART_STAR_INFO_GET_ERROR);
        }
        return response.getResult();
    }

    //补帧实现
    @Override
    public List<ChartDataVO> frameInsert(List<ChartDataVO> chartDataList) {
        if (CollectionUtil.isEmpty(chartDataList)){
            return chartDataList;
        }

        //进行补帧，按天来补
        Date startDate = DateUtil.parse(chartDataList.get(0).getxValue(), "yyyyMMdd");
        Date endDate = DateUtil.parse(chartDataList.get(chartDataList.size()-1).getxValue(), "yyyyMMdd");
        ChartDataVO lastChartData = chartDataList.get(0);
        ChartDataVO chartDataItem;
        List<ChartDataVO> resultList = new ArrayList<>();
        for (Date stepDate = startDate; stepDate.compareTo(endDate) <= 0; stepDate = DateUtil.offsetDay(stepDate, 1)){
            chartDataItem = new ChartDataVO();
            chartDataItem.setDataRecordId(lastChartData.getDataRecordId());
            chartDataItem.setUserId(lastChartData.getUserId());
            chartDataItem.setGitSite(lastChartData.getGitSite());
            chartDataItem.setOwner(lastChartData.getOwner());
            chartDataItem.setRepo(lastChartData.getRepo());
            chartDataItem.setMetricsType(lastChartData.getMetricsType());
            chartDataItem.setxValue(DateUtil.format(stepDate, "yyyyMMdd"));
            chartDataItem.setCreateDate(new Date());

            if (chartDataList.contains(chartDataItem)){
                lastChartData = chartDataList.get(chartDataList.indexOf(chartDataItem));
                resultList.add(lastChartData);
            }else{
                chartDataItem.setyValue(lastChartData.getyValue());
                resultList.add(chartDataItem);
            }
        }
        return resultList;
    }

    @Override
    protected boolean isSupportIncrement() {
        return true;
    }

    @Override
    public String getTaskStr() {
        return StrUtil.format("{}_{}", GitSite.GITEE.getCode(), ChartMetrics.STARS.getCode());
    }
}
