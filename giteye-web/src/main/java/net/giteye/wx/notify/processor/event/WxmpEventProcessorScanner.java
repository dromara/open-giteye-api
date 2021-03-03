package net.giteye.wx.notify.processor.event;

import net.giteye.wx.notify.processor.msg.WxmpMsgProcessor;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * 微信事件类处理扫描器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class WxmpEventProcessorScanner implements BeanPostProcessor, PriorityOrdered {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (WxmpEventProcessor.class.isAssignableFrom(bean.getClass())) {
            log.info("扫描到微信公众号通知Event处理器[{}]", beanName);
            WxmpEventProcessorManager.loadInstance().putProcessor((WxmpEventProcessor) bean);
        }
        return bean;
    }
}
