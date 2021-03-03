package net.giteye.vo;

import java.util.Map;

public class SimpleChartDataVO extends BaseVO{

    private String x;

    private Integer y;

    private Map<String,Object> extData;

    public SimpleChartDataVO(String x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }
}
