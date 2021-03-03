package net.giteye.dto;

import net.giteye.enums.ChartMetrics;
import net.giteye.enums.ChartType;
import net.giteye.vo.RepositoryVO;

import java.io.Serializable;

/**
 * 图表生成参数
 *
 * @author gongjun[dt_flys@hotmail.com]
 */
public class ChartGenerateDTO implements Serializable {

    /**
     * Git仓库
     */
    private RepositoryVO repository;

    /**
     * 指标
     */
    private ChartMetrics metrics;

    /**
     * 图表类型
     */
    private ChartType type;

    public RepositoryVO getRepository() {
        return repository;
    }

    public void setRepository(RepositoryVO repository) {
        this.repository = repository;
    }

    public ChartMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(ChartMetrics metrics) {
        this.metrics = metrics;
    }

    public ChartType getType() {
        return type;
    }

    public void setType(ChartType type) {
        this.type = type;
    }
}
