package net.giteye.task;

import cn.hutool.cron.CronUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * 任务启动器
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public class TaskInit implements InitializingBean {

    @Resource
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        String env = environment.getActiveProfiles()[0];
        if (env.equals("prod")){
            CronUtil.start();
        }
    }
}
