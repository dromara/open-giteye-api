package net.giteye.wx.notify.processor.msg;

import net.giteye.wx.enums.WxmpMsgEnum;

import java.util.Map;

/**
 * 微信消息类处理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface WxmpMsgProcessor {

    String process(Map<String, Object> body);

    WxmpMsgEnum getWxmpMsgEnum();

}
