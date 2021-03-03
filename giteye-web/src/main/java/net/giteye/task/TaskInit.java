package net.giteye.task;

import cn.hutool.cron.CronUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * 任务启动器
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public class TaskInit implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        CronUtil.start();
    }
}
