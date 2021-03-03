package net.giteye.wx.enums;

public enum WxmpEventEnum {

    SUBSCRIBE("subscribe", "订阅"),
    UNSUBSCRIBE("unsubscribe", "取消订阅"),
    SCAN("scan", "扫描二维码"),
    CLICK("click", "自定义菜单"),
    VIEW("view", "菜单跳转链接"),
    OTHER("other", "其他");

    private String code;

    private String name;

    WxmpEventEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WxmpEventEnum getEnumByCode(String code) {
        for (WxmpEventEnum e : WxmpEventEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
