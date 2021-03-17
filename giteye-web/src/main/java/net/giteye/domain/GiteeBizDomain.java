package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.HttpStatus;
import com.google.common.collect.Lists;
import net.giteye.api.session.SessionUtil;
import net.giteye.client.dto.GiteeAuthTokenDTO;
import net.giteye.client.gitee.GiteeAuthClient;
import net.giteye.client.gitee.GiteeHtmlClient;
import net.giteye.client.gitee.GiteeRepoClient;
import net.giteye.client.gitee.GiteeUserClient;
import net.giteye.vo.*;
import net.giteye.db.entity.GiteeUserAuth;
import net.giteye.db.service.GiteeUserAuthService;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.property.GeProperty;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private GiteeHtmlClient giteeHtmlClient;

    @Resource
    private GeProperty geProperty;

    @Resource
    private GiteeUserAuthService giteeUserAuthService;

    public String getGiteeCallbackUrl(Long userId) {
        String callbackUrl = StrUtil.format("{}?userid={}", geProperty.getGiteeCallbackUrl(), userId);
        return callbackUrl;
    }

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

    public String getFinalGiteeAuthUrlWithoutLogin() {
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();
        GiteeUserAuthVO giteeUserAuthVO = getGiteeUserAuthFromDB(userInfoVO.getId());

        String finalGiteeAuthUrlWithoutLogin = StrUtil.format("{}?client_id={}&redirect_uri={}&response_type=code&scope={}",
                geProperty.getGiteeAuthUrl(),
                geProperty.getGiteeClientId(),
                getGiteeCallbackUrl(),
                URLUtil.encode(giteeUserAuthVO.getScope()));
        return finalGiteeAuthUrlWithoutLogin;
    }

    public void persistGiteeAuth(String code, Long userId) {
        //用授权码去gitee获取access code
        GiteeAuthTokenDTO dto = new GiteeAuthTokenDTO();
        dto.setCode(code);
        dto.setRedirectUri(getGiteeCallbackUrl(userId));
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
        record.setCode(code);
        record.setAccessToken(accessTokenVO.getAccessToken());
        //这里减去(4小时）是为了防止边界情况，所以提前1小时使其失效
        record.setExpiresTime(DateUtil.offsetSecond(new Date(), accessTokenVO.getExpiresIn() - 3600*4));
        record.setRefreshToken(accessTokenVO.getRefreshToken());
        record.setScope(accessTokenVO.getScope());
        record.setGiteeCreateDate(giteeUserInfoVO.getCreatedAt());
        record.setHtmlUrl(giteeUserInfoVO.getHtmlUrl());
        record.setUpdateDate(new Date());
        record.setCreateDate(new Date());
        giteeUserAuthService.saveOrUpdate(record);
    }

    public GiteeUserAuthVO getUserAvailableUserAuth(Long userId){
        GiteeUserAuthVO giteeUserAuthVO = getGiteeUserAuthFromDB(userId);;

        //判断是否授权过期，如果过期，则重新获取并更新
        if (DateUtil.compare(new Date(), giteeUserAuthVO.getExpiresTime()) >= 0) {
            ForestResponse<GiteeAccessTokenVO> forestResponse = giteeAuthClient.refreshAuthToken(giteeUserAuthVO.getRefreshToken());
            if (forestResponse.isError()){
                throw new GeException(GeErrorCode.GITEE_REFRESH_TOKEN_ERROR);
            }

            GiteeAccessTokenVO accessTokenVO = forestResponse.getResult();

            //更新giteeUserAuth
            giteeUserAuthVO.setAccessToken(accessTokenVO.getAccessToken());
            giteeUserAuthVO.setRefreshToken(accessTokenVO.getRefreshToken());
            //这里减去3600秒(1小时）是为了防止边界情况，所以提前1小时使其失效
            giteeUserAuthVO.setExpiresTime(DateUtil.offsetSecond(new Date(), accessTokenVO.getExpiresIn() - 3600));
            giteeUserAuthVO.setUpdateDate(new Date());
            giteeUserAuthService.updateById(BeanUtil.copyProperties(giteeUserAuthVO, GiteeUserAuth.class));
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
        pageSize = 100;
        ForestResponse<List<GiteeRepoVO>> repoResp = giteeUserClient.getAuthUserRepos(accessToken, page, pageSize, SearchValue);

        if (repoResp.isError()) {
            if (repoResp.getStatusCode() == HttpStatus.UNAUTHORIZED){
                forceRefreshToken(accessToken);
                throw new GeException(GeErrorCode.GITEE_TOKEN_ERROR);
            }

            throw new GeException(GeErrorCode.GITEE_REPO_GET_ERROR);
        }

        List<GiteeRepoVO> list = repoResp.getResult();

        //fork的仓库不显示
        list = list.stream().filter(giteeRepoVO -> {
            if(giteeRepoVO.isFork() || giteeRepoVO.isHide()){
                return false;
            }else{
                return true;
            }
        }).collect(Collectors.toList());

        return list;
    }

    public GiteeRepoVO getGiteeUserRepo(String accessToken, String owner, String repo){
        ForestResponse<GiteeRepoVO> repoResp = giteeRepoClient.getUserRepos(accessToken, owner, repo);
        if (repoResp.isError()){
            String errorMsg = StrUtil.format("[{}]{}", repo, repoResp.getContent());
            throw new GeException(GeErrorCode.GITEE_REPO_GET_ERROR, errorMsg);
        }
        return repoResp.getResult();
    }

    public List<StargazerInfoVO> loadRepoStars(String accessToken, String owner, String repo, int page, int pageSize) {
        ForestResponse<List<StargazerInfoVO>> response = giteeRepoClient.getRepoStars(accessToken,
                owner, repo, page, pageSize);
        if (response.isError()) {
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
                forceRefreshToken(accessToken);
                throw new GeException(GeErrorCode.GITEE_TOKEN_ERROR);
            }

            String errorMsg = StrUtil.format("[{}]{}", repo, response.getContent());
            throw new GeException(GeErrorCode.CHART_STAR_INFO_GET_ERROR, errorMsg);
        }
        return response.getResult();
    }

    public GiteeMetricsVO getGiteeMetrics(String accessToken, String owner, String repo){
        ForestResponse<GiteeMetricsVO> resp = giteeRepoClient.getRepoMetrics(accessToken, owner, repo);
        if (resp.isError()){
            if (resp.getStatusCode() == HttpStatus.UNAUTHORIZED){
                forceRefreshToken(accessToken);
                throw new GeException(GeErrorCode.GITEE_TOKEN_ERROR);
            }
            throw new GeException(GeErrorCode.GITEE_REPO_METRICS_GET_ERROR, resp.getContent());
        }
        return resp.getResult();
    }

    public void forceRefreshToken(String oldToken){
        QueryWrapper<GiteeUserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("access_token", oldToken));
        GiteeUserAuth giteeUserAuth = giteeUserAuthService.getOne(queryWrapper, false);

        if (ObjectUtil.isNull(giteeUserAuth)){
            return;
        }

        //现在马上过期
        giteeUserAuth.setExpiresTime(new Date());
        giteeUserAuthService.updateById(giteeUserAuth);

        //再拿一遍有用的gitee auth信息，拿的过程中会自动刷成最新的
        getUserAvailableUserAuth(giteeUserAuth.getUserId());
    }

    public GiteeUserAuthVO getGiteeUserAuthFromDB(Long userId){
        QueryWrapper<GiteeUserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userId));
        GiteeUserAuth giteeUserAuth = giteeUserAuthService.getOne(queryWrapper);
        if (ObjectUtil.isNull(giteeUserAuth)) {
            throw new GeException(GeErrorCode.GITEE_NOT_AUTH);
        }
        return BeanUtil.copyProperties(giteeUserAuth, GiteeUserAuthVO.class);
    }

    public List<ContributorVO> getGiteeContributors(String owner, String repo){
        //组装cookie map
        String[] cookieArray = geProperty.getCookieData().replace(" ","").split(";");
        Map<String, String> cookieMap = new HashMap<>();
        for (String cookieItem : cookieArray){
            cookieMap.put(cookieItem.split("=")[0],cookieItem.split("=")[1]);
        }

        //定义结果集
        List<ContributorVO> resultList = new ArrayList<>();

        //定义基本参数
        String requestBaseUrl = StrUtil.format("{}/{}/{}/contributors", geProperty.getGiteeBaseUrl(), owner, repo);
        String requestUrl,content,login,name,avatarUrl,htmlUrl;
        Elements elements;
        int page = 1;
        //利用访问页面循环page，获取内容
        while (true){
            try{
                requestUrl = StrUtil.format("{}?page={}", requestBaseUrl, page++);
                Connection connection = Jsoup.connect(requestUrl);
                connection.cookies(cookieMap);
                content = connection.execute().body();
                elements = Jsoup.parse(content).select("div[class=item user-list-item]");
                if (elements.isEmpty()){
                    break;
                }
                for (Element element : elements) {
                    name = element.select("div[class=content] > div > a").first().text();

                    avatarUrl = element.select("a > img").first().attr("src");

                    htmlUrl = element.select("a").first().attr("href");
                    if (!htmlUrl.startsWith("mailto")) {
                        htmlUrl = StrUtil.format("https://gitee.com{}", htmlUrl);
                    }

                    resultList.add(new ContributorVO(name, name, avatarUrl, htmlUrl));
                }
            }catch (Exception e){
                throw new GeException(GeErrorCode.GITEE_REPO_CONTRIBUTORS_GET_ERROR, e.getMessage());
            }
        }
        return resultList;
    }
}
