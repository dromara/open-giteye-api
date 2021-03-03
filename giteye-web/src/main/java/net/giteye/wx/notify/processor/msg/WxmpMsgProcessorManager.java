package net.giteye.wx.notify.processor.msg;

import net.giteye.wx.enums.WxmpMsgEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号消息处理管理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class WxmpMsgProcessorManager {

    private final static WxmpMsgProcessorManager instance = new WxmpMsgProcessorManager();

    private final Map<WxmpMsgEnum, WxmpMsgProcessor> processorMap = new HashMap<>();

    public static WxmpMsgProcessorManager loadInstance(){
        return instance;
    }

    public void putProcessor(WxmpMsgProcessor processor){
        processorMap.put(processor.getWxmpMsgEnum(), processor);
    }

    public WxmpMsgProcessor getProcessor(WxmpMsgEnum wxmpMsgEnum){
        return processorMap.get(wxmpMsgEnum);
    }
}
