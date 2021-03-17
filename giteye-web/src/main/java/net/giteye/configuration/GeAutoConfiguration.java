package net.giteye.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import net.giteye.springtools.SpringAware;
import net.giteye.task.ChartTaskScanner;
import net.giteye.task.TaskInit;
import net.giteye.web.interceptor.GeInterceptorConfig;
import net.giteye.wx.notify.processor.event.WxmpEventProcessorScanner;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessorScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

/**
 * giteye服务的自动装配类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@Configuration
public class GeAutoConfiguration {

    @Bean
    public GeInterceptorConfig geInterceptorConfig(){
        return new GeInterceptorConfig();
    }

    @Bean
    public WxmpMsgProcessorScanner wxmpMsgProcessorScanner(){
        return new WxmpMsgProcessorScanner();
    }

    @Bean
    public WxmpEventProcessorScanner wxmpEventProcessorScanner(){
        return new WxmpEventProcessorScanner();
    }

    @Bean
    public ChartTaskScanner chartTaskScanner(){
        return new ChartTaskScanner();
    }

    @Bean
    public SpringAware springAware(){
        return new SpringAware();
    }

    @Bean
    public TaskInit taskInit(){
        return new TaskInit();
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
