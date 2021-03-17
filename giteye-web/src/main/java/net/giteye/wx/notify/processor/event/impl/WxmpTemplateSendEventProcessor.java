package net.giteye.wx.notify.processor.event.impl;

import net.giteye.wx.enums.WxmpEventEnum;
import net.giteye.wx.notify.processor.event.WxmpEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信模板发送成功通知
 *
 * @author Bryan.Zhang
 * @since 2021/3/13
 */
@Component
public class WxmpTemplateSendEventProcessor implements WxmpEventProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String process(Map<String, Object> body) {
        log.info("发送给微信用户[{}]的模板消息已成功发送", body.get("FromUserName"));
        return "success";
    }

    @Override
    public WxmpEventEnum getWxmpEventEnum() {
        return WxmpEventEnum.TEMPLATE_SEND_JOB_FINISH;
    }
}
