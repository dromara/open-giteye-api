package net.giteye.client.wxmp;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.backend.ContentType;
import net.giteye.vo.WxmpAccessTokenVO;
import net.giteye.client.wxmp.interceptor.WxmpAccessTokenInterceptor;
import net.giteye.client.wxmp.interceptor.WxmpQRCodeInterceptor;
import net.giteye.vo.WxUserVO;
import net.giteye.vo.WxmpTemplateSendResultVO;
import net.giteye.vo.WxmpStarTemplateSendVO;

@BaseRequest(baseURL = "${wxmp.wxmpBaseUrl}")
public interface WxmpClient {

    @Get(
            url = "/cgi-bin/token?grant_type=client_credential&appid=${wxmp.appId}&secret=${wxmp.secret}",
            interceptor = WxmpAccessTokenInterceptor.class
    )
    WxmpAccessTokenVO getAccessToken();

    @Post(
            url = "/cgi-bin/qrcode/create?access_token=${token}",
            contentType = ContentType.APPLICATION_JSON,
            data = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"${scene}\"}}}",
            interceptor = WxmpQRCodeInterceptor.class
    )
    String generateQRCode(@DataVariable("token") String token, @DataVariable("scene") String scene);

    @Get(
            url = "/cgi-bin/user/info?access_token=${0}&openid=${1}&lang=zh_CN"
    )
    WxUserVO getWxUser(String token, String openId);

    @Post(
            url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${token}",
            contentType = ContentType.APPLICATION_JSON,
            data = "{\"touser\":\"${data.userOpenId}\",\"template_id\":\"${wxmp.starTemplateId}\",\"url\":\"https://gitee.com/${data.gitUserName}/${data.repo}\",\"data\":{\"first\":{\"value\":\"您的项目又获得了新的Star\",\"color\":\"#000000\"},\"keyword1\":{\"value\":\"${data.userName}\",\"color\":\"#000000\"},\"keyword2\":{\"value\":\"${data.timeStr}\",\"color\":\"#000000\"},\"keyword3\":{\"value\":\"${data.content}\",\"color\":\"#4a86e8\"},\"remark\":{\"value\":\"感谢你使用giteye.net\",\"color\":\"#000000\"}}}"
    )
    WxmpTemplateSendResultVO sendStarTemplateMsg(@DataVariable("token") String token, @DataVariable("data") WxmpStarTemplateSendVO data);
}
