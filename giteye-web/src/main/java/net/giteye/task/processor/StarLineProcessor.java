package net.giteye.task.processor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import net.giteye.domain.ChartDataBizDomain;
import net.giteye.springtools.SpringAware;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartDataVO;
import net.giteye.vo.StargazerInfoVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StarLineProcessor {

    private final List<ChartDataVO> chartDataVOList = new ArrayList<>();

    private final ChartDataBizDomain chartDataBizDomain = SpringAware.getBean(ChartDataBizDomain.class);

    public static StarLineProcessor newInstance() {
        return new StarLineProcessor();
    }

    public void process(List<StargazerInfoVO> stargazerInfoVOList, ChartDataRecordVO chartDataRecordVO) {
        int lastYValue = 0;

        //寻找上一个的lastYValue,如果有的话,则取上一个lastYValue
        ChartDataVO lastChartData = chartDataBizDomain.getMetricsLastChartData(chartDataRecordVO);

        if(ObjectUtil.isNotNull(lastChartData)){
            chartDataVOList.add(lastChartData);
            lastYValue = lastChartData.getyValue();
        }

        //把star的数据转换成按天统计的累计数据
        ChartDataVO chartDataVO;
        for (StargazerInfoVO StarInfoItem : stargazerInfoVOList) {
            chartDataVO = new ChartDataVO();
            chartDataVO.setDataRecordId(chartDataRecordVO.getId());
            chartDataVO.setUserId(chartDataRecordVO.getUserId());
            chartDataVO.setGitSite(chartDataRecordVO.getGitSite());
            chartDataVO.setOwner(chartDataRecordVO.getGitUsername());
            chartDataVO.setRepo(chartDataRecordVO.getRepoName());
            chartDataVO.setMetricsType(chartDataRecordVO.getMetricsType());
            chartDataVO.setxValue(DateUtil.format(StarInfoItem.getStarAt(), "yyyyMMdd"));
            chartDataVO.setCreateDate(new Date());

            if (chartDataVOList.contains(chartDataVO)) {
                ChartDataVO item = chartDataVOList.get(chartDataVOList.indexOf(chartDataVO));
                item.setyValue(item.getyValue() + 1);
            } else {
                if (CollectionUtil.isNotEmpty(chartDataVOList)) {
                    lastYValue = chartDataVOList.get(chartDataVOList.size() - 1).getyValue();
                }
                chartDataVO.setyValue(lastYValue + 1);
                chartDataVOList.add(chartDataVO);
            }
        }
    }

    public List<ChartDataVO> getChartDataList() {
        return chartDataVOList;
    }
}
