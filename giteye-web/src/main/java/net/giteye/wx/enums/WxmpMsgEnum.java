package net.giteye.wx.enums;

/**
 * 微信通知类型
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public enum WxmpMsgEnum {

    TEXT("text", "文本"),
    EVENT("event", "事件"),
    OTHER("other", "其他")
    ;

    private String code;

    private String name;

    WxmpMsgEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WxmpMsgEnum getEnumByCode(String code) {
        for (WxmpMsgEnum e : WxmpMsgEnum.values()) {
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
