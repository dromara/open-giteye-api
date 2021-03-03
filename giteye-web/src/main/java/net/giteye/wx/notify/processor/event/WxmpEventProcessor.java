package net.giteye.wx.notify.processor.event;

import net.giteye.wx.enums.WxmpEventEnum;

import java.util.Map;

/**
 * 微信公众号事件类处理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface WxmpEventProcessor {

    String process(Map<String, Object> body);

    WxmpEventEnum getWxmpEventEnum();
}
