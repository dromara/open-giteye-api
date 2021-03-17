package net.giteye.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * giteye的springboot配置参数
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "giteye", ignoreUnknownFields = true)
public class GeProperty {

    private String wxmpToken;

    private String wxmpEncodingAesKey;

    private String wxmpAppId;

    private String wxmpSecret;

    private String giteeBaseUrl;

    private String giteeAuthUrl;

    private String giteeCallbackUrl;

    private String giteeRedirectUrl;

    private String giteeClientId;

    private String giteeClientSecret;

    private String headlessChromeDriver;

    private Integer chartWebWidth;

    private Integer chartWebHeight;

    private String sessionDomain;

    private Long defaultAuthUserId;

    private String starTemplateId;

    private String cookieData;

    public String getWxmpToken() {
        return wxmpToken;
    }

    public void setWxmpToken(String wxmpToken) {
        this.wxmpToken = wxmpToken;
    }

    public String getWxmpEncodingAesKey() {
        return wxmpEncodingAesKey;
    }

    public void setWxmpEncodingAesKey(String wxmpEncodingAesKey) {
        this.wxmpEncodingAesKey = wxmpEncodingAesKey;
    }

    public String getWxmpAppId() {
        return wxmpAppId;
    }

    public void setWxmpAppId(String wxmpAppId) {
        this.wxmpAppId = wxmpAppId;
    }

    public String getWxmpSecret() {
        return wxmpSecret;
    }

    public void setWxmpSecret(String wxmpSecret) {
        this.wxmpSecret = wxmpSecret;
    }

    public String getGiteeAuthUrl() {
        return giteeAuthUrl;
    }

    public void setGiteeAuthUrl(String giteeAuthUrl) {
        this.giteeAuthUrl = giteeAuthUrl;
    }

    public String getGiteeCallbackUrl() {
        return giteeCallbackUrl;
    }

    public void setGiteeCallbackUrl(String giteeCallbackUrl) {
        this.giteeCallbackUrl = giteeCallbackUrl;
    }

    public String getGiteeRedirectUrl() {
        return giteeRedirectUrl;
    }

    public void setGiteeRedirectUrl(String giteeRedirectUrl) {
        this.giteeRedirectUrl = giteeRedirectUrl;
    }

    public String getGiteeBaseUrl() {
        return giteeBaseUrl;
    }

    public void setGiteeBaseUrl(String giteeBaseUrl) {
        this.giteeBaseUrl = giteeBaseUrl;
    }

    public String getGiteeClientId() {
        return giteeClientId;
    }

    public void setGiteeClientId(String giteeClientId) {
        this.giteeClientId = giteeClientId;
    }

    public String getGiteeClientSecret() {
        return giteeClientSecret;
    }

    public void setGiteeClientSecret(String giteeClientSecret) {
        this.giteeClientSecret = giteeClientSecret;
    }

    public String getHeadlessChromeDriver() {
        return headlessChromeDriver;
    }

    public void setHeadlessChromeDriver(String headlessChromeDriver) {
        this.headlessChromeDriver = headlessChromeDriver;
    }

    public Integer getChartWebWidth() {
        return chartWebWidth;
    }

    public void setChartWebWidth(Integer chartWebWidth) {
        this.chartWebWidth = chartWebWidth;
    }

    public Integer getChartWebHeight() {
        return chartWebHeight;
    }

    public void setChartWebHeight(Integer chartWebHeight) {
        this.chartWebHeight = chartWebHeight;
    }

    public String getSessionDomain() {
        return sessionDomain;
    }

    public void setSessionDomain(String sessionDomain) {
        this.sessionDomain = sessionDomain;
    }

    public String getCookieData() {
        return cookieData;
    }

    public void setCookieData(String cookieData) {
        this.cookieData = cookieData;
    }

    public Long getDefaultAuthUserId() {
        return defaultAuthUserId;
    }

    public void setDefaultAuthUserId(Long defaultAuthUserId) {
        this.defaultAuthUserId = defaultAuthUserId;
    }

    public String getStarTemplateId() {
        return starTemplateId;
    }

    public void setStarTemplateId(String starTemplateId) {
        this.starTemplateId = starTemplateId;
    }
}
