package net.giteye.client.gitee;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.LogEnabled;
import com.dtflys.forest.http.ForestResponse;
import net.giteye.client.gitee.interceptor.GiteeMetricsGetInterceptor;
import net.giteye.client.gitee.interceptor.GiteeReposGetInterceptor;
import net.giteye.vo.ContributorVO;
import net.giteye.vo.GiteeMetricsVO;
import net.giteye.vo.GiteeRepoVO;
import net.giteye.vo.StargazerInfoVO;

import java.util.List;

@BaseRequest(baseURL = "${giteye.giteeBaseUrl}/api/v5/repos")
public interface GiteeRepoClient {

    @Get(
            url = "/${owner}/${repo}?access_token=${accessToken}",
            interceptor = GiteeReposGetInterceptor.class
    )
    @LogEnabled(logResponseContent = false)
    ForestResponse<GiteeRepoVO> getUserRepos(@DataVariable("accessToken") String accessToken,
                                             @DataVariable("owner") String owner,
                                             @DataVariable("repo") String repo);

    @Get(
            url = "/${owner}/${repo}/git/gitee_metrics?access_token=${accessToken}",
            interceptor = GiteeMetricsGetInterceptor.class
    )
    @LogEnabled(logResponseContent = false)
    ForestResponse<GiteeMetricsVO> getRepoMetrics(@DataVariable("accessToken") String accessToken,
                                                  @DataVariable("owner") String owner,
                                                  @DataVariable("repo") String repo);

    @Get(
            url = "/${owner}/${repo}/stargazers?access_token=${accessToken}&page=${page}&per_page=${pageSize}"
    )
    @LogEnabled(logResponseContent = false)
    ForestResponse<List<StargazerInfoVO>> getRepoStars(@DataVariable("accessToken") String accessToken,
                                                      @DataVariable("owner") String owner,
                                                      @DataVariable("repo") String repo,
                                                      @DataVariable("page") int page,
                                                      @DataVariable("pageSize") int pageSize);

    @Get(
            url = "/${owner}/${repo}/collaborators?access_token=${accessToken}"
    )
    @LogEnabled(logResponseContent = false)
    ForestResponse<List<ContributorVO>> getContributors(@DataVariable("accessToken") String accessToken,
                                                        @DataVariable("owner") String owner,
                                                        @DataVariable("repo") String repo);
}
