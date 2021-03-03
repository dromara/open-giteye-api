package net.giteye.client.wxmp;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.backend.ContentType;
import net.giteye.vo.WxmpAccessTokenVO;
import net.giteye.client.wxmp.interceptor.WxmpAccessTokenInterceptor;
import net.giteye.client.wxmp.interceptor.WxmpQRCodeInterceptor;
import net.giteye.vo.WxUserVO;

@BaseRequest(baseURL = "${wxmp.wxmpBaseUrl}")
public interface WxmpClient {

    @Get(
            url = "/cgi-bin/token?grant_type=client_credential&appid=${wxmp.appId}&secret=${wxmp.secret}",
            interceptor = WxmpAccessTokenInterceptor.class
    )
    WxmpAccessTokenVO getAccessToken();

    @Post(
            url = "/cgi-bin/qrcode/create?access_token=${0}",
            contentType = ContentType.APPLICATION_JSON,
            data = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"${1}\"}}}",
            interceptor = WxmpQRCodeInterceptor.class
    )
    String generateQRCode(String token, String scene);

    @Get(
            url = "/cgi-bin/user/info?access_token=${0}&openid=${1}&lang=zh_CN"
    )
    WxUserVO getWxUser(String token, String openId);
}
