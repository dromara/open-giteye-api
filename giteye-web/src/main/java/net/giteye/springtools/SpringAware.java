package net.giteye.springtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Spring上下文处理类
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public class SpringAware implements ApplicationContextAware{

	private static final Logger log = LoggerFactory.getLogger(SpringAware.class);

	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return (T) applicationContext.getBean(clazz);
	}

	public static void rollBack(){
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}

	public static <T> T registerBean(Class<T> c){
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		BeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClassName(c.getName());
		beanFactory.registerBeanDefinition(c.getName(),beanDefinition);
		return getBean(c.getName());
	}
}
