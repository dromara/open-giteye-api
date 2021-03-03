package net.giteye.client.gitee;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.http.ForestResponse;
import net.giteye.client.dto.GiteeAuthTokenDTO;
import net.giteye.vo.GiteeAccessTokenVO;

/**
 * gitee授权接口客户端
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@BaseRequest(baseURL = "${giteye.giteeBaseUrl}/oauth")
public interface GiteeAuthClient {

    @Post(
            url = "/token?grant_type=authorization_code&code=${dto.code}&client_id=${giteye.giteeClientId}&redirect_uri=${dto.redirectUri}",
            data = "client_secret=${giteye.giteeClientSecret}"
    )
    ForestResponse<GiteeAccessTokenVO> getAuthToken(@DataVariable("dto") GiteeAuthTokenDTO giteeAuthTokenDTO);

    @Post(
            url = "/token?grant_type=refresh_token&refresh_token=${refreshToken}"
    )
    ForestResponse<GiteeAccessTokenVO> refreshAuthToken(@DataVariable("refreshToken") String refreshToken);
}
