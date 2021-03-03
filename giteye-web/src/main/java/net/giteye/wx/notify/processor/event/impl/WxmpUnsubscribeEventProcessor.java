package net.giteye.wx.notify.processor.event.impl;

import net.giteye.domain.WxmpBizDomain;
import net.giteye.wx.enums.WxmpEventEnum;
import net.giteye.wx.notify.processor.event.WxmpEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 微信公众号关注事件处理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@Component
public class WxmpUnsubscribeEventProcessor implements WxmpEventProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxmpBizDomain wxmpBizDomain;

    @Override
    public String process(Map<String, Object> body) {
        log.info("进入到取消订阅事件类处理器");
        String openId = body.get("FromUserName").toString();
        wxmpBizDomain.unsubscribe(openId);
        return "success";
    }

    @Override
    public WxmpEventEnum getWxmpEventEnum() {
        return WxmpEventEnum.UNSUBSCRIBE;
    }
}
