package net.giteye.enums;

/**
 * 图表统计的指标
 *
 * @author gongjun[dt_flys@hotmail.com]
 */
public enum ChartMetrics {

    STARS("stars", "star趋势图"),

    REPO_RADAR("repo_radar", "指数图"),

    CONTRIBUTORS("contributors", "项目贡献表")
    ;

    private final String code;

    private final String name;

    ChartMetrics(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ChartMetrics getEnumByCode(String code) {
        for (ChartMetrics e : ChartMetrics.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
