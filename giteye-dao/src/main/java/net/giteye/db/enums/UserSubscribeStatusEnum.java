package net.giteye.db.enums;

public enum UserSubscribeStatusEnum {

    SUBSCRIBE(1, "已关注"),
    UNSUBSCRIBE(0, "已消关")
    ;

    private Integer code;

    private String name;

    UserSubscribeStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UserSubscribeStatusEnum getEnumByCode(Integer code) {
        for (UserSubscribeStatusEnum e : UserSubscribeStatusEnum.values()) {
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
