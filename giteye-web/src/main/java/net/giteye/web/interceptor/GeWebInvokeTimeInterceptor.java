package net.giteye.web.interceptor;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web的调用时间统计拦截器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class GeWebInvokeTimeInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private InheritableThreadLocal<StopWatch> invokeTimeTL = new InheritableThreadLocal<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            String url = request.getRequestURI();
            String parameters = JSON.toJSONString(request.getParameterMap());
            log.info("开始请求URL[{}],IP为:{},参数为:{}", url, request.getRemoteAddr() ,parameters);

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            StopWatch stopWatch = invokeTimeTL.get();
            stopWatch.stop();
            log.info("结束URL[{}]的调用,耗时为:{}毫秒", request.getRequestURI(), stopWatch.getTotalTimeMillis());
            invokeTimeTL.remove();
        }
    }
}
