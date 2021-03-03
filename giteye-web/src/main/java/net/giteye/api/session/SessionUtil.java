package net.giteye.api.session;

import cn.hutool.core.util.ObjectUtil;
import net.giteye.db.entity.UserInfo;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.vo.GiteeUserAuthVO;
import net.giteye.vo.GiteeUserInfoVO;
import net.giteye.vo.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 会话工具
 *
 * @author Bryan.Zhang
 * @since 2021/1/29
 */
public class SessionUtil {

    private static Logger log = LoggerFactory.getLogger(SessionUtil.class);

    public static UserInfoVO getSessionUser(){
        HttpSession session = getSession();
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute("userInfo");
        if (ObjectUtil.isNull(userInfoVO)){
            HttpServletResponse response = getResponse();
            response.addCookie(SessionUtil.newCookie("nick_name",null,0));
            response.addCookie(SessionUtil.newCookie("head_img_url",null,0));
            throw new GeException(GeErrorCode.SESSION_INVALID);
        }
        return userInfoVO;
    }

    public static void setSessionUser(UserInfoVO userInfoVO){
        getSession().setAttribute("userInfo", userInfoVO);
    }

    public static GiteeUserAuthVO getGiteeUserAuth(){
        //获取giteeUser是有可能获取不到的，并不一定是登陆了就一定能获取到，有可能没授权
        try{
            HttpSession session = getSession();
            GiteeUserAuthVO giteeUserAuth = (GiteeUserAuthVO) session.getAttribute("giteeUserAuth");
            return giteeUserAuth;
        }catch (Exception e){
            return null;
        }
    }

    public static void setGiteeUserAuth(GiteeUserAuthVO giteeUserAuth){
        try{
            getSession().setAttribute("giteeUserAuth", giteeUserAuth);
        }catch (Exception e){}
    }

    public static String getSessionId(){
        return getSession().getId();
    }

    public static HttpSession getSession(){
        try{
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            return session;
        }catch (Exception e){
            throw new GeException(GeErrorCode.SESSION_ERROR);
        }
    }

    public static HttpServletResponse getResponse(){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public static Cookie newCookie(String key, String value, int days) {
        Cookie cookie = new Cookie(key, value);
        cookie.setDomain("giteye.net");
        cookie.setMaxAge(days * 24 * 60 * 60);
        cookie.setPath("/");
        return cookie;
    }
}
