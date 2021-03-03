package net.giteye.enums;

/**
 * 图表主题
 *
 * @author Bryan.Zhang
 * @since 2021/2/4
 */
public enum ChartTheme {

    NONE("none", "none"),
    VINTAGE("vintage", "VINTAGE"),
    DARK("dark", "DARK"),
    WESTEROS("westeros", "WESTEROS"),
    ESSOS("essos", "ESSOS"),
    WONDERLAND("wonderland", "WONDERLAND"),
    WALDEN("walden", "WALDEN"),
    CHALK("chalk", "CHALK"),
    INFOGRAPHIC("infographic", "INFOGRAPHIC"),
    MACARONS("macarons", "MACARONS"),
    ROMA("roma", "ROMA"),
    SHINE("shine", "SHINE"),
    PURPLE("purple","PURPLE")
    ;

    private final String code;

    private final String name;

    ChartTheme(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ChartTheme getEnumByCode(String code) {
        for (ChartTheme e : ChartTheme.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
