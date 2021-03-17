package net.giteye.task.gitee;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import net.giteye.domain.GiteeBizDomain;
import net.giteye.enums.ChartMetrics;
import net.giteye.enums.GitSite;
import net.giteye.task.AbsChartTask;
import net.giteye.vo.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiteeContributorTask extends AbsChartTask {

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Override
    protected List<ChartDataVO> process(GiteeUserAuthVO giteeUserAuthVO, ChartDataRecordVO chartDataRecordVO) {
        List<ContributorVO> contributorVOList = giteeBizDomain.getGiteeContributors(chartDataRecordVO.getGitUsername(), chartDataRecordVO.getRepoName());
        List<ChartDataVO> chartDataVOList = new ArrayList<>();
        ChartDataVO chartDataVO;
        for (ContributorVO item : contributorVOList){
            chartDataVO = initChartData(chartDataRecordVO);
            chartDataVO.setxValue(item.getName());
            chartDataVO.setExtData(JSON.toJSONString(item));
            chartDataVOList.add(chartDataVO);
        }
        return chartDataVOList;
    }

    @Override
    protected boolean isSupportIncrement() {
        return false;
    }

    @Override
    public String getTaskStr() {
        return StrUtil.format("{}_{}", GitSite.GITEE.getCode(), ChartMetrics.CONTRIBUTORS.getCode());
    }
}
