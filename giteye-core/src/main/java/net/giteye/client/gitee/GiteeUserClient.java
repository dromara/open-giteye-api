package net.giteye.client.gitee;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.LogEnabled;
import com.dtflys.forest.http.ForestResponse;
import net.giteye.client.gitee.interceptor.GiteeReposGetInterceptor;
import net.giteye.vo.GiteeRepoVO;
import net.giteye.vo.GiteeUserInfoVO;

import java.util.List;

@BaseRequest(baseURL = "${giteye.giteeBaseUrl}/api/v5")
public interface GiteeUserClient {

    @Get(
            url = "user?access_token=${accessToken}"
    )
    ForestResponse<GiteeUserInfoVO> getAuthUserInfo(@DataVariable("accessToken") String accessToken);

    @Get(
            url = "users/${userName}?access_token=${accessToken}"
    )
    ForestResponse<GiteeUserInfoVO> getAuthUserInfo(@DataVariable("accessToken") String accessToken,
                                                    @DataVariable("userName") String userName);

    @Get(
            url = "user/repos?access_token=${accessToken}&type=owner&sort=created&page=${page}&per_page=${pageSize}&q=${searchValue}",
            interceptor = GiteeReposGetInterceptor.class
    )
    @LogEnabled(logResponseContent = false)
    ForestResponse<List<GiteeRepoVO>> getAuthUserRepos(@DataVariable("accessToken") String accessToken,
                                                       @DataVariable("page") int page,
                                                       @DataVariable("pageSize") int pageSize,
                                                       @DataVariable("searchValue") String searchValue);

}
