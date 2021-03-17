package net.giteye.api.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import net.giteye.api.resp.ApiResp;
import net.giteye.api.session.SessionUtil;
import net.giteye.domain.AuthBizDomain;
import net.giteye.domain.GiteeBizDomain;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.vo.GiteeUserAuthVO;
import net.giteye.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 授权类控制器
 *
 * @author Bryan.Zhang
 * @since 2021/1/25
 */
@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Resource
    private AuthBizDomain authBizDomain;

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @RequestMapping(value = "/generate_qrcode", method = RequestMethod.GET)
    public ApiResp<String> generateQRCode(@CookieValue(name = "client_id", defaultValue = "0") String clientId,
                                          HttpServletResponse response) {
        //如果cookie里没有，则生成一个，设置到cookie里
        if (clientId.equals("0")) {
            clientId = IdUtil.simpleUUID();

            //设置cookie
            Cookie cookie = SessionUtil.newCookie("client_id", clientId, 30);
            response.addCookie(cookie);
        }

        //获得微信二维码
        String qrCodeUrl = authBizDomain.generateQRCode(clientId);

        return ApiResp.success(qrCodeUrl);
    }

    @RequestMapping(value = "/poll", method = RequestMethod.GET)
    public ApiResp<Boolean> poll(@CookieValue(name = "client_id", defaultValue = "0") String clientId,
                                    HttpServletResponse response) {
        if (clientId.equals("0")) {
            throw new GeException(GeErrorCode.CLIENT_ID_INVALID);
        }

        UserInfoVO userInfoVO = authBizDomain.pollClientLogin(clientId);
        if (ObjectUtil.isNull(userInfoVO)) {
            return ApiResp.success(false);
        }

        //把user的信息放入session
        SessionUtil.setSessionUser(userInfoVO);

        //把user的部分信息放入cookie，重新设置JSESSIONID的有效时间为3天
        response.addCookie(SessionUtil.newCookie("nick_name", URLUtil.encode(userInfoVO.getWxNickName()), 3));
        response.addCookie(SessionUtil.newCookie("head_img_url", userInfoVO.getHeadImgUrl(), 3));
        response.addCookie(SessionUtil.newCookie("JSESSIONID", SessionUtil.getSessionId(), 3));

        return ApiResp.success(true);
    }
}
