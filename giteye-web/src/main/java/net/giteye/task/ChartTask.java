package net.giteye.task;

import net.giteye.enums.GitSite;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartDataVO;
import net.giteye.vo.ChartRecordVO;

import java.util.List;

/**
 * 图表数据任务的接口
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public interface ChartTask {

    //生成一个图表
    void generateOneChart(ChartDataRecordVO chartDataRecordVO, boolean refreshAllData);

    void check(ChartDataRecordVO chartDataRecordVO);

    List<ChartDataVO> frameInsert(List<ChartDataVO> chartDataList);

    String getTaskStr();

}
