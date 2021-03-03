package net.giteye.task.gitee;

import cn.hutool.core.util.StrUtil;
import net.giteye.domain.GiteeBizDomain;
import net.giteye.enums.ChartMetrics;
import net.giteye.enums.GitSite;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.task.AbsChartTask;
import net.giteye.vo.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GiteeRepoRadarTask extends AbsChartTask {

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Override
    protected List<ChartDataVO> process(GiteeUserAuthVO giteeUserAuthVO, ChartDataRecordVO chartDataRecordVO) {
        GiteeMetricsVO giteeMetricsVO = giteeBizDomain.getGiteeMetrics(giteeUserAuthVO.getAccessToken(),
                chartDataRecordVO.getGitUsername(), chartDataRecordVO.getRepoName());

        List<ChartDataVO> chartDataVOList = new ArrayList<>();
        ChartDataVO chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("代码活跃度\n{}(>{}%)",giteeMetricsVO.getVitality(),giteeMetricsVO.getVitalityPercent()));
        chartDataVO.setyValue(giteeMetricsVO.getVitality());
        chartDataVOList.add(chartDataVO);
        chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("影响力\n{}(>{}%)",giteeMetricsVO.getInfluence(),giteeMetricsVO.getInfluencePercent()));
        chartDataVO.setyValue(giteeMetricsVO.getInfluence());
        chartDataVOList.add(chartDataVO);
        chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("流行趋势\n{}(>{}%)",giteeMetricsVO.getTrend(),giteeMetricsVO.getTrendPercent()));
        chartDataVO.setyValue(giteeMetricsVO.getTrend());
        chartDataVOList.add(chartDataVO);
        chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("团队健康\n{}(>{}%)",giteeMetricsVO.getHealth(),giteeMetricsVO.getHealthPercent()));
        chartDataVO.setyValue(giteeMetricsVO.getHealth());
        chartDataVOList.add(chartDataVO);
        chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("社区活跃度\n{}(>{}%)",giteeMetricsVO.getCommunity(),giteeMetricsVO.getCommunityPercent()));
        chartDataVO.setyValue(giteeMetricsVO.getCommunity());
        chartDataVOList.add(chartDataVO);
        chartDataVO = initChartData(chartDataRecordVO);
        chartDataVO.setxValue(StrUtil.format("综合得分:{}",giteeMetricsVO.getTotalScore()));
        chartDataVO.setyValue(giteeMetricsVO.getTotalScore());
        chartDataVOList.add(chartDataVO);

        //设置是否支持增量
        chartDataRecordVO.setSupportIncrement(false);

        return chartDataVOList;
    }

    @Override
    public String getTaskStr() {
        return StrUtil.format("{}_{}", GitSite.GITEE.getCode(), ChartMetrics.REPO_RADAR.getCode());
    }

    @Override
    public void check(ChartDataRecordVO chartDataRecordVO) {
        String accessToken = giteeBizDomain.getCurrUserAvailableAccessToken();

        //拿到当前仓库信息
        GiteeRepoVO giteeRepoVO = giteeBizDomain.getGiteeUserRepo(accessToken, chartDataRecordVO.getGitUsername(), chartDataRecordVO.getRepoName());

        if (!giteeRepoVO.isRecommend()){
            throw new GeException(GeErrorCode.CHART_METRICS_NOT_RECOMMEND);
        }
    }

    @Override
    protected boolean isSupportIncrement() {
        return false;
    }
}
