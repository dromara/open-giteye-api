package net.giteye.exception;

/**
 * giteye的异常码枚举类
 * 关于code的定义规则，前2位表示类别，比如01表示授权类别，02表示生成图片逻辑类别，后2位为顺序码
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public enum GeErrorCode {
    UNKNOWN_EXCEPTION("0000", "未检查的异常"),
    CLIENT_ID_INVALID("0001", "clientId检测异常"),
    CANNOT_GET_LOGIN_USER("0002", "无法获取登陆用户异常"),
    SESSION_ERROR("0003", "会话异常"),
    SESSION_INVALID("0004", "会话已失效"),

    WX_CANNOT_GET_USER("0101", "没法获取微信用户异常"),
    WX_USER_STATUS_INVALID("0102", "用户状态为不可用"),

    GITEE_AUTH_CODE_GET_ERROR("0200", "GITEE授权码获得异常"),
    GITEE_NOT_AUTH("0201", "Gitee还未授权"),
    GITEE_USER_INFO_GET_ERROR("0202", "Gitee用户信息获取异常"),
    GITEE_REPO_GET_ERROR("0203", "Gitee用户仓库列表获取异常"),
    GITEE_REPO_METRICS_GET_ERROR("0204", "Gitee用户仓库指数获取异常"),
    GITEE_REPO_CONTRIBUTORS_GET_ERROR("0205", "Gitee仓库贡献者列表获取异常"),

    CHART_RECORD_IS_EXIST("0301", "此类型图表已存在"),
    CHART_STAR_INFO_GET_ERROR("0302", "star数信息获取异常"),
    CHART_UUID_ERROR("0303", "UUID错误"),
    CHART_METRICS_ERROR("0304", "不存在的指标"),
    CHART_TYPE_ERROR("0305", "不存在的图表类型"),
    CHART_THEME_ERROR("0306", "不存在的图表主题"),
    CHART_METRICS_INFO_GET_ERROR("0307", "项目能力图获取异常"),
    CHART_METRICS_NOT_RECOMMEND("0308", "项目还未被推荐")
    ;

    private String code;
    private String message;

    GeErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GeErrorCode getEnumByCode(String code) {
        for (GeErrorCode e : GeErrorCode.values()) {
            if (e.getErrorCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getErrorCode().toString();
    }

    public String getErrorCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
