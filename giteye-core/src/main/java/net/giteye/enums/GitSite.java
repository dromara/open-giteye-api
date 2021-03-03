package net.giteye.enums;

/**
 * Git站点
 */
public enum GitSite {

    /**
     * Gitee
     */
    GITEE("gitee", "https://gitee.com"),

    /**
     * Github
     */
    GITHUB("github", "https://github.com"),
    ;

    /**
     * 站点名称
     */
    private final String code;

    /**
     * 域名
     */
    private final String domain;

    GitSite(String code, String domain) {
        this.code = code;
        this.domain = domain;
    }

    public String getCode() {
        return code;
    }

    public String getDomain() {
        return domain;
    }

    public static GitSite getEnumByCode(String code) {
        for (GitSite e : GitSite.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
