package net.giteye.enums;

/**
 * 图表类型
 *
 * @author gongjun[dt_flys@hotmail.com]
 */
public enum ChartType {

    NONE("none", "无"),

    LINE("line", "折线图"),

    RADAR("radar", "雷达图")
    ;

    private final String code;

    private final String name;

    ChartType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ChartType getEnumByCode(String code) {
        for (ChartType e : ChartType.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
