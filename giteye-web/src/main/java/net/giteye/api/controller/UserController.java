package net.giteye.api.controller;

import cn.hutool.core.util.ObjectUtil;
import net.giteye.api.resp.ApiResp;
import net.giteye.api.session.SessionUtil;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.vo.UserInfoVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @RequestMapping(value = "/get_login_user", method = RequestMethod.GET)
    public ApiResp<UserInfoVO> getLoginUser(){
        UserInfoVO userInfoVO = SessionUtil.getSessionUser();
        if (ObjectUtil.isNull(userInfoVO)){
            throw new GeException(GeErrorCode.CANNOT_GET_LOGIN_USER);
        }
        return ApiResp.success(userInfoVO);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResp<Object> logout(HttpServletResponse response) {
        HttpSession session = SessionUtil.getSession();
        session.invalidate();
        response.addCookie(SessionUtil.newCookie("nick_name",null,0));
        response.addCookie(SessionUtil.newCookie("head_img_url",null,0));
        return ApiResp.success();
    }
}
