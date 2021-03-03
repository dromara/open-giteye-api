package net.giteye.task;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import net.giteye.domain.ChartDataBizDomain;
import net.giteye.domain.ChartDataRecordBizDomain;
import net.giteye.domain.ChartRecordBizDomain;
import net.giteye.enums.GitSite;
import net.giteye.springtools.SpringAware;
import net.giteye.task.gitee.GiteeStarLineTask;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务总入口
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public class TaskMain {

    private static Logger log = LoggerFactory.getLogger(TaskMain.class);

    private final ChartDataRecordBizDomain chartDataRecordBizDomain = SpringAware.getBean(ChartDataRecordBizDomain.class);

    private final ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(10,20);

    public void runAll4Increment() {
        log.info("--------开始增量更新任务--------");

        //获得图表任务管理器
        ChartTaskManager chartTaskManager = ChartTaskManager.loadInstance();

        List<ChartDataRecordVO> chartDataRecordVOList = chartDataRecordBizDomain.getCanIncrementChartRecord();
        for (ChartDataRecordVO item : chartDataRecordVOList){
            threadPoolExecutor.execute(() -> {
                ChartTask chartTask = chartTaskManager.getChartTask(chartTaskManager.getTaskStr(item));
                chartTask.generateOneChart(item, false);
            });
        }
    }

    public void runAll() {
        log.info("--------开始全量更新任务--------");

        //获得图表任务管理器
        ChartTaskManager chartTaskManager = ChartTaskManager.loadInstance();

        List<ChartDataRecordVO> chartDataRecordVOList = chartDataRecordBizDomain.getCanRefreshChartRecord();
        for (ChartDataRecordVO item : chartDataRecordVOList){
            threadPoolExecutor.execute(() -> {
                ChartTask chartTask = chartTaskManager.getChartTask(chartTaskManager.getTaskStr(item));
                chartTask.generateOneChart(item, true);
            });
        }
    }

}
