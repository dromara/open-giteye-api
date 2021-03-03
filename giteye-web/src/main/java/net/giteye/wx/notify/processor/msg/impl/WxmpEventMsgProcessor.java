package net.giteye.wx.notify.processor.msg.impl;

import cn.hutool.core.util.ObjectUtil;
import net.giteye.wx.enums.WxmpEventEnum;
import net.giteye.wx.enums.WxmpMsgEnum;
import net.giteye.wx.notify.processor.event.WxmpEventProcessor;
import net.giteye.wx.notify.processor.event.WxmpEventProcessorManager;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信公众号事件类处理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@Component
public class WxmpEventMsgProcessor implements WxmpMsgProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String process(Map<String, Object> body) {
        log.info("进入到事件类处理器");
        String eventStr = body.get("Event").toString().toLowerCase();
        WxmpEventEnum event = WxmpEventEnum.getEnumByCode(eventStr);
        if (ObjectUtil.isNull(event)){
            event = WxmpEventEnum.OTHER;
        }
        WxmpEventProcessor processor = WxmpEventProcessorManager.loadInstance().getProcessor(event);
        return processor.process(body);
    }

    @Override
    public WxmpMsgEnum getWxmpMsgEnum() {
        return WxmpMsgEnum.EVENT;
    }
}
