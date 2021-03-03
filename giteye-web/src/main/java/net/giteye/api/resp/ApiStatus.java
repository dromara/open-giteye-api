package net.giteye.api.resp;

public enum ApiStatus {

    SUCCESS("success", "成功"),
    FAILED("failed", "失败");

    private String code;

    private String name;

    ApiStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ApiStatus getEnumByCode(String code) {
        for (ApiStatus e : ApiStatus.values()) {
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
