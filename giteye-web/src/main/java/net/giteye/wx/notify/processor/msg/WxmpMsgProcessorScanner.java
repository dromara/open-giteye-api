package net.giteye.wx.notify.processor.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * 微信公众号消息处理扫描器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class WxmpMsgProcessorScanner implements BeanPostProcessor, PriorityOrdered {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (WxmpMsgProcessor.class.isAssignableFrom(bean.getClass())) {
            log.info("扫描到微信公众号通知MSG处理器[{}]", beanName);
            WxmpMsgProcessorManager.loadInstance().putProcessor((WxmpMsgProcessor) bean);
        }
        return bean;
    }
}
