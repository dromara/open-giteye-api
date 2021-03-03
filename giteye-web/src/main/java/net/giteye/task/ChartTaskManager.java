package net.giteye.task;

import cn.hutool.core.util.StrUtil;
import net.giteye.enums.ChartMetrics;
import net.giteye.enums.ChartType;
import net.giteye.enums.GitSite;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartRecordVO;

import java.util.HashMap;
import java.util.Map;

/**
 * ChartTask管理器
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public class ChartTaskManager {

    private static final ChartTaskManager instance = new ChartTaskManager();

    private final Map<String, ChartTask> chartTaskMap = new HashMap<>();

    public static ChartTaskManager loadInstance(){
        return instance;
    }

    public ChartTask getChartTask(String str){
        return chartTaskMap.get(str);
    }

    public void putChartTask(ChartTask chartTask){
        chartTaskMap.put(chartTask.getTaskStr(), chartTask);
    }

    public String getTaskStr(ChartDataRecordVO chartDataRecordVO){
        return StrUtil.format("{}_{}", chartDataRecordVO.getGitSite(), chartDataRecordVO.getMetricsType());
    }
}
