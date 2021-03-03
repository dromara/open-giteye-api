package net.giteye.wx.notify.processor.msg.impl;

import net.giteye.wx.enums.WxmpMsgEnum;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信公众号其他类消息处理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@Component
public class WxmpOtherMsgProcessor implements WxmpMsgProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String process(Map<String, Object> body) {
        log.info("进入到其他类消息处理器");
        return "success";
    }

    @Override
    public WxmpMsgEnum getWxmpMsgEnum() {
        return WxmpMsgEnum.OTHER;
    }
}
