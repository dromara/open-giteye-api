package net.giteye.api.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.giteye.api.resp.ApiResp;
import net.giteye.domain.ChartDataBizDomain;
import net.giteye.domain.ChartDataRecordBizDomain;
import net.giteye.domain.ChartRecordBizDomain;
import net.giteye.enums.ChartMetrics;
import net.giteye.enums.ChartTheme;
import net.giteye.enums.ChartType;
import net.giteye.enums.GitSite;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.task.ChartTask;
import net.giteye.task.ChartTaskManager;
import net.giteye.vo.*;
import net.giteye.domain.GiteeBizDomain;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Gitee的服务端API
 *
 * @author Bryan.Zhang
 * @since 2021/1/29
 */
@RestController
@RequestMapping(path = "/gitee")
public class GiteeController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Resource
    private ChartRecordBizDomain chartRecordBizDomain;

    @Resource
    private ChartDataBizDomain chartDataBizDomain;

    @Resource
    private ChartDataRecordBizDomain chartDataRecordBizDomain;

    private ThreadPoolExecutor threadPool = ThreadUtil.newExecutor(5,10);

    @RequestMapping(value = "/auth_url", method = RequestMethod.GET)
    public ApiResp<String> getGiteeAuthUrl() {
        return ApiResp.success(giteeBizDomain.getFinalGiteeAuthUrl());
    }

    @RequestMapping(value = "/auth_url_direct", method = RequestMethod.GET)
    public ApiResp<String> getGiteeAuthUrlDirect() {
        return ApiResp.success(giteeBizDomain.getFinalGiteeAuthUrlWithoutLogin());
    }

    @RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public ApiResp<GiteeUserInfoVO> currentUser() {
        //获取当前有效的access_token
        String accessToken = giteeBizDomain.getCurrUserAvailableAccessToken();

        //获取Gitee用户信息
        GiteeUserInfoVO giteeUserInfoVO = giteeBizDomain.getGiteeAuthUserInfo(accessToken);

        return ApiResp.success(giteeUserInfoVO);
    }

    @RequestMapping(value = "/user/is_auth", method = RequestMethod.GET)
    public ApiResp<Boolean> isAuth() {
        try {
            giteeBizDomain.getCurrUserAvailableUserAuth();
            return ApiResp.success(true);
        } catch (Exception e) {
            return ApiResp.success(false);
        }
    }

    @RequestMapping(value = "/user/repos", method = RequestMethod.GET)
    public ApiResp<List<GiteeRepoVO>> getAuthUserRepos(@RequestParam int page, @RequestParam int pageSize,
                                                       @RequestParam(required = false) String searchValue) {
        //获取当前有效的access_token
        String accessToken = giteeBizDomain.getCurrUserAvailableAccessToken();

        //获取Gitee授权用户仓库列表
        List<GiteeRepoVO> repoList = giteeBizDomain.getGiteeAuthUserRepos(accessToken, page, pageSize, searchValue);

        return ApiResp.success(repoList);
    }

    @RequestMapping(value = "/chart/list", method = RequestMethod.GET)
    public ApiResp<List<ChartRecordVO>> getCurrUserChart(){
        List<ChartRecordVO> resultList = chartRecordBizDomain.getCurrUserChartRecord();
        return ApiResp.success(resultList);
    }

    @RequestMapping(value = "/chart/{owner}/{repo}/{metricsType}/{chartType}/{theme}", method = RequestMethod.POST)
    public ApiResp<String> generateChart(@PathVariable("owner") String owner,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("metricsType") String metricsType,
                                         @PathVariable("chartType") String chartType,
                                         @PathVariable("theme") String theme,
                                         @RequestParam(required = false) boolean frame){
        //获取Gitee授权信息
        GiteeUserAuthVO giteeUserAuthVO = giteeBizDomain.getCurrUserAvailableUserAuth();

        ChartMetrics metricsEnum = ChartMetrics.getEnumByCode(metricsType);
        if (ObjectUtil.isNull(metricsEnum)){
            throw new GeException(GeErrorCode.CHART_METRICS_ERROR);
        }

        ChartType chartTypeEnum = ChartType.getEnumByCode(chartType);
        if (ObjectUtil.isNull(chartTypeEnum)){
            throw new GeException(GeErrorCode.CHART_TYPE_ERROR);
        }

        ChartTheme chartThemeEnum = ChartTheme.getEnumByCode(theme);
        if (ObjectUtil.isNull(chartThemeEnum)){
            throw new GeException(GeErrorCode.CHART_THEME_ERROR);
        }

        //检查这个图表记录是否已存在
        if (chartRecordBizDomain.checkChartRecordIsExist(GitSite.getEnumByCode("gitee"),
                giteeUserAuthVO.getLogin(), repo, metricsEnum, chartTypeEnum, chartThemeEnum)) {
            throw new GeException(GeErrorCode.CHART_RECORD_IS_EXIST);
        }

        String uuid = chartRecordBizDomain.generateChartJob(GitSite.GITEE,
                owner,
                repo,
                metricsEnum,
                chartTypeEnum,
                chartThemeEnum,
                frame);

        return ApiResp.success(uuid);
    }

    @RequestMapping(value = "/chart/record/{uuid}", method = RequestMethod.GET)
    public ApiResp<ChartRecordVO> getCurrUserChart(@PathVariable("uuid") String uuid){
        ChartRecordVO chartRecordVO = chartRecordBizDomain.getChartRecordByUUID(uuid);
        if (ObjectUtil.isNull(chartRecordVO)){
            throw new GeException(GeErrorCode.CHART_UUID_ERROR);
        }
        return ApiResp.success(chartRecordVO);
    }

    @RequestMapping(value = "/chart/data/{uuid}", method = RequestMethod.GET)
    public ApiResp<Collection<SimpleChartDataVO>> getChartData(@PathVariable("uuid") String uuid){
        //获取图表记录
        ChartRecordVO chartRecordVO = chartRecordBizDomain.getChartRecordByUUID(uuid);

        //获取到图表记录
        ChartDataRecordVO chartDataRecordVO = chartDataRecordBizDomain.getChartDataRecordByChartRecord(chartRecordVO);
        if (ObjectUtil.isNull(chartDataRecordVO)){
            throw new GeException(GeErrorCode.CHART_UUID_ERROR);
        }

        //获取到图表数据
        List<ChartDataVO> dataList = chartDataBizDomain.getChartData(chartDataRecordVO);

        //补帧操作，目前只有趋势图才会有补帧实现，其他图补帧为空实现
        if (chartRecordVO.getFrame()){
            ChartTask chartTask = ChartTaskManager.loadInstance().getChartTask(ChartTaskManager.loadInstance().getTaskStr(chartDataRecordVO));
            dataList = chartTask.frameInsert(dataList);
        }

        //转换成简单对象
        Collection<SimpleChartDataVO> resultList = CollectionUtils.collect(dataList, input -> {
            SimpleChartDataVO resultVO = new SimpleChartDataVO(input.getxValue(), input.getyValue());
            if (ObjectUtil.isNotEmpty(input.getExtData())){
                resultVO.setExtData(JSON.parseObject(input.getExtData(), new TypeReference<Map<String, Object>>(){}));
            }
            return resultVO;
        });

        return ApiResp.success(resultList);
    }
}
