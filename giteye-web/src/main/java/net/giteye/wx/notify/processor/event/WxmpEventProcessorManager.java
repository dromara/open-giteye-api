package net.giteye.wx.notify.processor.event;

import net.giteye.wx.enums.WxmpEventEnum;
import net.giteye.wx.enums.WxmpMsgEnum;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号事件类处理管理器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class WxmpEventProcessorManager {

    private final static WxmpEventProcessorManager instance = new WxmpEventProcessorManager();

    private final Map<WxmpEventEnum, WxmpEventProcessor> processorMap = new HashMap<>();

    public static WxmpEventProcessorManager loadInstance(){
        return instance;
    }

    public void putProcessor(WxmpEventProcessor processor){
        processorMap.put(processor.getWxmpEventEnum(), processor);
    }

    public WxmpEventProcessor getProcessor(WxmpEventEnum wxmpEventEnum){
        return processorMap.get(wxmpEventEnum);
    }
}
