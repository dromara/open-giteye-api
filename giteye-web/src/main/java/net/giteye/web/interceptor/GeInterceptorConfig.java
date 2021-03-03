package net.giteye.web.interceptor;

import net.giteye.property.GeProperty;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * webconfig类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class GeInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private GeProperty geProperty;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration1 = registry.addInterceptor(new GeWebInvokeTimeInterceptor());
        registration1.excludePathPatterns("/health/**");
        InterceptorRegistration registration2 = registry.addInterceptor(new GeSessionInterceptor());
        registration2.excludePathPatterns("/health/**",
                "/notify/**",
                geProperty.getGiteeRedirectUrl(),
                "/auth/**",
                "/chart/img/**",
                "/gitee/chart/data/**",
                "/gitee/chart/record/**",
                "/task/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
