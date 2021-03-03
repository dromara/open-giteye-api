package net.giteye.enums;

/**
 * 图表状态枚举类
 *
 * @author gongjun[dt_flys@hotmail.com]
 */
public enum ChartRecordStatus {

    INIT(0, "初始化"),

    GENERATING(1, "生成中"),

    UPDATING(2, "更新中"),

    UPLOADED(3, "已生成"),

    FAILED(-1, "生成失败"),
    ;

    private final Integer code;

    private final String name;


    ChartRecordStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ChartRecordStatus getEnumByCode(Integer code) {
        for (ChartRecordStatus e : ChartRecordStatus.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
