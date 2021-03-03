package net.giteye;

import net.giteye.charts.hchrome.HeadlessChromeImageGenerator;
import net.giteye.charts.hchrome.WebDriverPool;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-16 21:12
 */
public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);

    private volatile Connector connector;

    private final int waitTime = 10;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        logger.info("close event:{}", contextClosedEvent);
        this.connector.pause();

        logger.info("[Giteye] 开始停机，销毁所有Chrome Driver");
        try {
            WebDriverPool pool = HeadlessChromeImageGenerator.getDriverPool();
            if (pool != null) {
                pool.clear();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }

        Executor executor = this.connector.getProtocolHandler().getExecutor();
        try {
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                    logger.warn("Tomcat 进程在" + waitTime + " 秒内无法结束，尝试强制结束");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }
}
