package net.giteye.db.enums;

public enum ClientLoginStatusEnum {

    VALID(1, "有效"),
    INVALID(0, "无效")
    ;

    private Integer code;

    private String name;

    ClientLoginStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ClientLoginStatusEnum getEnumByCode(Integer code) {
        for (ClientLoginStatusEnum e : ClientLoginStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
