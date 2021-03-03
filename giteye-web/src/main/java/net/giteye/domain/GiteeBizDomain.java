package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtflys.forest.http.ForestResponse;
import com.google.common.collect.Lists;
import net.giteye.api.session.SessionUtil;
import net.giteye.client.dto.GiteeAuthTokenDTO;
import net.giteye.client.gitee.GiteeAuthClient;
import net.giteye.client.gitee.GiteeRepoClient;
import net.giteye.client.gitee.GiteeUserClient;
import net.giteye.vo.*;
import net.giteye.db.entity.GiteeUserAuth;
import net.giteye.db.service.GiteeUserAuthService;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.property.GeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Gitee业务
 *
 * @author Bryan.Zhang
 * @since 2021/1/25
 */
@Component
@Transactional
public class GiteeBizDomain {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GiteeAuthClient giteeAuthClient;

    @Resource
    private GiteeUserClient giteeUserClient;

    @Resource
    private GiteeRepoClient giteeRepoClient;

    @Resource
    private GeProperty geProperty;

    @Resource
    private GiteeUserAuthService giteeUserAuthService;

    public String getGiteeCallbackUrl() {
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();
        String callbackUrl = StrUtil.format("{}?userid={}", geProperty.getGiteeCallbackUrl(), userInfoVO.getId());
        return callbackUrl;
    }

    public String getFinalGiteeAuthUrl() {
        String finalGiteeAuthUrl = StrUtil.format("{}?client_id={}&redirect_uri={}&response_type=code",
                geProperty.getGiteeAuthUrl(),
                geProperty.getGiteeClientId(),
                getGiteeCallbackUrl());
        return finalGiteeAuthUrl;
    }

    public void persistGiteeAuth(String code, Long userId) {
        //用授权码去gitee获取access code
        GiteeAuthTokenDTO dto = new GiteeAuthTokenDTO();
        dto.setCode(code);
        dto.setRedirectUri(getGiteeCallbackUrl());
        ForestResponse<GiteeAccessTokenVO> authResp = giteeAuthClient.getAuthToken(dto);
        if (authResp.isError()) {
            throw new GeException(GeErrorCode.GITEE_AUTH_CODE_GET_ERROR, authResp.getContent());
        }

        GiteeAccessTokenVO accessTokenVO = authResp.getResult();

        //查看当前用户下有没有Gitee的授权记录
        QueryWrapper<GiteeUserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userId));
        GiteeUserAuth giteeUserAuth = giteeUserAuthService.getOne(queryWrapper);

        //查询Gitee的用户信息
        GiteeUserInfoVO giteeUserInfoVO = getGiteeAuthUserInfo(accessTokenVO.getAccessToken());

        //持久化Gitee的Auth信息
        GiteeUserAuth record = new GiteeUserAuth();
        if (ObjectUtil.isNotNull(giteeUserAuth)) {
            record.setId(giteeUserAuth.getId());
        }
        record.setUserId(userId);
        record.setLogin(giteeUserInfoVO.getLogin());
        record.setName(giteeUserInfoVO.getName());
        record.setEmail(giteeUserInfoVO.getEmail());
        record.setAccessToken(accessTokenVO.getAccessToken());
        //这里减去3600秒(1小时）是为了防止边界情况，所以提前1小时使其失效
        record.setExpiresTime(DateUtil.offsetSecond(new Date(), accessTokenVO.getExpiresIn() - 3600));
        record.setRefreshToken(accessTokenVO.getRefreshToken());
        record.setScope(accessTokenVO.getScope());
        record.setGiteeCreateDate(giteeUserInfoVO.getCreatedAt());
        record.setHtmlUrl(giteeUserInfoVO.getHtmlUrl());
        record.setUpdateDate(new Date());
        record.setCreateDate(new Date());
        giteeUserAuthService.saveOrUpdate(record);

        //设置giteeUserInfo到session中
        SessionUtil.setGiteeUserAuth(BeanUtil.copyProperties(giteeUserAuth, GiteeUserAuthVO.class));
    }

    public GiteeUserAuthVO getUserAvailableUserAuth(Long userId){
        //先从session里取，如果没有，则从数据库中取
        GiteeUserAuthVO giteeUserAuthVO = SessionUtil.getGiteeUserAuth();
        if (ObjectUtil.isNull(giteeUserAuthVO) || StrUtil.isEmpty(giteeUserAuthVO.getAccessToken())){
            giteeUserAuthVO = getGiteeUserAuthFromDB(userId);

            //更新session里的Gitee授权信息
            SessionUtil.setGiteeUserAuth(giteeUserAuthVO);
        }

        //判断是否授权过期，如果过期，则重新获取并更新
        if (DateUtil.compare(new Date(), giteeUserAuthVO.getExpiresTime()) >= 0) {
            ForestResponse<GiteeAccessTokenVO> forestResponse = giteeAuthClient.refreshAuthToken(giteeUserAuthVO.getRefreshToken());
            GiteeAccessTokenVO accessTokenVO = forestResponse.getResult();

            //更新giteeUserAuth
            giteeUserAuthVO.setAccessToken(accessTokenVO.getAccessToken());
            giteeUserAuthVO.setRefreshToken(accessTokenVO.getRefreshToken());
            //这里减去3600秒(1小时）是为了防止边界情况，所以提前1小时使其失效
            giteeUserAuthVO.setExpiresTime(DateUtil.offsetSecond(new Date(), accessTokenVO.getExpiresIn() - 3600));
            giteeUserAuthVO.setUpdateDate(new Date());
            giteeUserAuthService.updateById(BeanUtil.copyProperties(giteeUserAuthVO, GiteeUserAuth.class));

            //更新session里的Gitee授权信息
            SessionUtil.setGiteeUserAuth(giteeUserAuthVO);
        }

        return giteeUserAuthVO;
    }

    public GiteeUserAuthVO getCurrUserAvailableUserAuth(){
        //先从session里取到userInfo
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();

        return getUserAvailableUserAuth(userInfoVO.getId());
    }

    public String getCurrUserAvailableAccessToken() {
        GiteeUserAuthVO giteeUserAuthVO = getCurrUserAvailableUserAuth();
        return giteeUserAuthVO.getAccessToken();
    }

    public GiteeUserInfoVO getGiteeAuthUserInfo(String accessToken) {
        ForestResponse<GiteeUserInfoVO> userResp = giteeUserClient.getAuthUserInfo(accessToken);
        if (userResp.isError()) {
            throw new GeException(GeErrorCode.GITEE_USER_INFO_GET_ERROR, userResp.getContent());
        }
        return userResp.getResult();
    }

    public GiteeUserInfoVO getGiteeAuthUserInfo(String accessToken, String userName){
        ForestResponse<GiteeUserInfoVO> response = giteeUserClient.getAuthUserInfo(accessToken, userName);
        if (response.isError()){
            return null;
        }
        return response.getResult();
    }

    public List<GiteeRepoVO> getGiteeAuthUserRepos(String accessToken, int page, int pageSize, String SearchValue) {
        ForestResponse<List<GiteeRepoVO>> repoResp = giteeUserClient.getAuthUserRepos(accessToken, page, pageSize, SearchValue);
        return repoResp.getResult();
    }

    public GiteeRepoVO getGiteeUserRepo(String accessToken, String owner, String repo){
        ForestResponse<GiteeRepoVO> repoResp = giteeRepoClient.getUserRepos(accessToken, owner, repo);
        if (repoResp.isError()){
            throw new GeException(GeErrorCode.GITEE_REPO_GET_ERROR, repoResp.getContent());
        }
        return repoResp.getResult();
    }

    public GiteeMetricsVO getGiteeMetrics(String accessToken, String owner, String repo){
        ForestResponse<GiteeMetricsVO> resp = giteeRepoClient.getRepoMetrics(accessToken, owner, repo);
        if (resp.isError()){
            throw new GeException(GeErrorCode.GITEE_REPO_METRICS_GET_ERROR, resp.getContent());
        }
        return resp.getResult();
    }

    public List<ContributorVO> getGiteeContributors(String accessToken, String owner, String repo){
        ForestResponse<List<ContributorVO>> response = giteeRepoClient.getContributors(accessToken, owner, repo);
        if (response.isError()){
            throw new GeException(GeErrorCode.GITEE_REPO_CONTRIBUTORS_GET_ERROR);
        }

        //因为gitee返回的列表作者是在最后一个，所以要把最后一个移到第一位
        List<ContributorVO> list = response.getResult();
        ContributorVO author = list.remove(list.size()-1);
        list.add(0,author);
        return list;
    }

    private GiteeUserAuthVO getGiteeUserAuthFromDB(Long userId){
        QueryWrapper<GiteeUserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userId));
        GiteeUserAuth giteeUserAuth = giteeUserAuthService.getOne(queryWrapper);
        if (ObjectUtil.isNull(giteeUserAuth)) {
            throw new GeException(GeErrorCode.GITEE_NOT_AUTH);
        }
        return BeanUtil.copyProperties(giteeUserAuth, GiteeUserAuthVO.class);
    }
}
