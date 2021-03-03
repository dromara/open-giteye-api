package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.giteye.api.session.SessionUtil;
import net.giteye.charts.utils.SerialsUtil;
import net.giteye.db.entity.ChartRecord;
import net.giteye.db.service.ChartRecordService;
import net.giteye.enums.*;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.task.ChartTask;
import net.giteye.task.ChartTaskManager;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartRecordVO;
import net.giteye.vo.GiteeUserAuthVO;
import net.giteye.vo.UserInfoVO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Transactional
public class ChartRecordBizDomain {

    @Resource
    private ChartRecordService chartRecordService;

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Resource
    private ChartDataBizDomain chartDataBizDomain;

    private final ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(5, 10);

    public boolean checkChartRecordIsExist(GitSite gitSite, String owner, String repo, ChartMetrics metricsType, ChartType chartType, ChartTheme theme) {
        //获得当前用户
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();

        QueryWrapper<ChartRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userInfoVO.getId()))
                .and(wrapper -> wrapper.eq("git_site", gitSite.getCode()))
                .and(wrapper -> wrapper.eq("git_username", owner))
                .and(wrapper -> wrapper.eq("repo_name", repo))
                .and(wrapper -> wrapper.eq("metrics_type", metricsType.getCode()))
                .and(wrapper -> wrapper.eq("chart_type", chartType.getCode()))
                .and(wrapper -> wrapper.eq("theme", theme.getCode()));
        int cnt = chartRecordService.count(queryWrapper);
        return cnt != 0;
    }

    public ChartRecordVO getChartRecordByUUID(String UUID) {
        QueryWrapper<ChartRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("img_uuid", UUID));
        ChartRecord chartRecord = chartRecordService.getOne(queryWrapper);
        if (ObjectUtil.isNull(chartRecord)) {
            return null;
        }
        return BeanUtil.copyProperties(chartRecord, ChartRecordVO.class);
    }

    public String generateChartJob(GitSite gitSite, String owner, String repo, ChartMetrics metricsType, ChartType chartType, ChartTheme theme, boolean frame) {
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();

        //获取当前gitee的授权信息
        GiteeUserAuthVO giteeUserAuthVO = giteeBizDomain.getCurrUserAvailableUserAuth();

        //生成chartRecord
        ChartRecord chartRecord = new ChartRecord();
        chartRecord.setUserId(userInfoVO.getId());
        chartRecord.setChartName(StrUtil.format("[{}]-{}", repo, metricsType.getName()));
        chartRecord.setGitSite(gitSite.getCode());
        chartRecord.setGitUsername(owner);
        chartRecord.setRepoName(repo);
        chartRecord.setRepoUrl(StrUtil.format("{}/{}", giteeUserAuthVO.getHtmlUrl(), repo));
        chartRecord.setMetricsType(metricsType.getCode());
        chartRecord.setChartType(chartType.getCode());
        chartRecord.setTheme(theme.getCode());
        chartRecord.setStatus(ChartRecordStatus.INIT.getCode());
        chartRecord.setCreateTime(new Date());
        chartRecord.setUpdateTime(new Date());
        chartRecord.setImgUuid(SerialsUtil.generate8UUID());
        chartRecord.setFrame(frame);
        chartRecordService.save(chartRecord);

        ChartRecordVO chartRecordVO = BeanUtil.copyProperties(chartRecord, ChartRecordVO.class);

        //插入chartDataRecord
        ChartDataRecordVO chartDataRecordVO = chartDataBizDomain.queryOrInsertChartDataRecord(chartRecordVO);

        //拿到任务执行器
        String taskStr = ChartTaskManager.loadInstance().getTaskStr(chartDataRecordVO);
        ChartTask chartTask = ChartTaskManager.loadInstance().getChartTask(taskStr);

        //进行检查，实现了检查的task实现会执行，默认不实现的
        chartTask.check(chartDataRecordVO);

        //运行任务
        threadPoolExecutor.execute(() -> chartTask.generateOneChart(chartDataRecordVO, true));

        return chartRecordVO.getImgUuid();
    }

    public void updateChartRecord(ChartRecordVO recordVO) {
        chartRecordService.updateById(BeanUtil.copyProperties(recordVO, ChartRecord.class));
    }

    public List<ChartRecordVO> queryChartRecordByChartDataRecord(ChartDataRecordVO chartDataRecordVO){
        QueryWrapper<ChartRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", chartDataRecordVO.getUserId()))
                .and(wrapper -> wrapper.eq("git_site", chartDataRecordVO.getGitSite()))
                .and(wrapper -> wrapper.eq("git_username", chartDataRecordVO.getGitUsername()))
                .and(wrapper -> wrapper.eq("repo_name", chartDataRecordVO.getRepoName()))
                .and(wrapper -> wrapper.eq("metrics_type", chartDataRecordVO.getMetricsType()))
                .and(wrapper -> wrapper.notIn("status", ChartRecordStatus.GENERATING.getCode(), ChartRecordStatus.UPDATING.getCode()));

        List<ChartRecord> list = chartRecordService.list(queryWrapper);
        List<ChartRecordVO> resultList = new ArrayList<>();
        for (ChartRecord chartRecord : list){
            resultList.add(BeanUtil.copyProperties(chartRecord, ChartRecordVO.class));
        }
        return resultList;
    }

    public List<ChartRecordVO> getCurrUserChartRecord() {
        //获取当前用户
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();

        //获取当前用户的Gitee已生成的图表记录
        QueryWrapper<ChartRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userInfoVO.getId()));
        queryWrapper.orderByDesc("id");
        List<ChartRecord> list = chartRecordService.list(queryWrapper);

        List<ChartRecordVO> resultList = new ArrayList<>();
        for (ChartRecord chartRecord : list) {
            resultList.add(BeanUtil.copyProperties(chartRecord, ChartRecordVO.class));
        }
        return resultList;
    }

}
