package net.giteye.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云的配置
 *
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-04 0:42
 */
@ConfigurationProperties(prefix = "qcloud", ignoreUnknownFields = true)
public class QCloudProperty {

    private String secretId;

    private String secretKey;

    private String cosRegionId;

    private String cosGiteeBucket;

    private String cosBaseUrl;

    private String chartUrl;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCosRegionId() {
        return cosRegionId;
    }

    public void setCosRegionId(String cosRegionId) {
        this.cosRegionId = cosRegionId;
    }

    public String getCosGiteeBucket() {
        return cosGiteeBucket;
    }

    public void setCosGiteeBucket(String cosGiteeBucket) {
        this.cosGiteeBucket = cosGiteeBucket;
    }

    public String getCosBaseUrl() {
        return cosBaseUrl;
    }

    public void setCosBaseUrl(String cosBaseUrl) {
        this.cosBaseUrl = cosBaseUrl;
    }

    public String getChartUrl() {
        return chartUrl;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }
}
