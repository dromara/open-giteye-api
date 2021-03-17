package net.giteye.client.gitee;

import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.http.ForestResponse;
import net.giteye.client.gitee.interceptor.GiteeHtmlCurrentStarsInterceptor;

/**
 * Gitee基于html的接口
 *
 * @author Bryan.Zhang
 * @since 2021/3/15
 */
public interface GiteeHtmlClient {

    @Get(
            url = "${giteye.giteeBaseUrl}/${owner}/${repo}/statistic?type=Star",
            interceptor = GiteeHtmlCurrentStarsInterceptor.class
    )
    ForestResponse<Integer> getTodayRepoIncrementStars(@DataVariable("owner") String owner, @DataVariable("repo") String repo);
}
