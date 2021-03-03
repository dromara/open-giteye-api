package net.giteye.notify.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import net.giteye.domain.GiteeBizDomain;
import net.giteye.property.GeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 *
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@Controller
@RequestMapping(path = "/notify")
public class GitAuthNotifyController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GiteeBizDomain giteeBizDomain;

    @Resource
    private GeProperty geProperty;

    @RequestMapping(value = "/gitee", method = RequestMethod.GET)
    public ModelAndView giteeNotify(@RequestParam(value = "code") String code,
                                    @RequestParam(value = "userid") Long userId){
        Assert.notBlank(code);

        //持久化Gitee的授权信息
        giteeBizDomain.persistGiteeAuth(code, userId);

        //需要redirect到某个页面
        ModelAndView modelAndView = new ModelAndView(StrUtil.format("redirect:{}",geProperty.getGiteeRedirectUrl()));
        return modelAndView;
    }
}
