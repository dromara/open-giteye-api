package net.giteye.vo;

import java.io.Serializable;
import java.util.Date;

public class ChartRecordVO extends BaseVO{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String chartName;

    private Long userId;

    private String gitSite;

    private String gitUsername;

    private String repoName;

    private String repoUrl;

    private String metricsType;

    private String chartType;

    private String theme;

    private String imgUuid;

    private String imgType;

    private String imgFileName;

    private String imgUrl;

    private String imgThumbUrl;

    private String msg;

    private Integer isCombine;

    private Long combineId;

    private Boolean frame;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long generateConsume;

    private Long uploadConsume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGitSite() {
        return gitSite;
    }

    public void setGitSite(String gitSite) {
        this.gitSite = gitSite;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getMetricsType() {
        return metricsType;
    }

    public void setMetricsType(String metricsType) {
        this.metricsType = metricsType;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImgUuid() {
        return imgUuid;
    }

    public void setImgUuid(String imgUuid) {
        this.imgUuid = imgUuid;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getIsCombine() {
        return isCombine;
    }

    public void setIsCombine(Integer isCombine) {
        this.isCombine = isCombine;
    }

    public Long getCombineId() {
        return combineId;
    }

    public void setCombineId(Long combineId) {
        this.combineId = combineId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUploadConsume() {
        return uploadConsume;
    }

    public void setUploadConsume(Long uploadConsume) {
        this.uploadConsume = uploadConsume;
    }

    public Long getGenerateConsume() {
        return generateConsume;
    }

    public void setGenerateConsume(Long generateConsume) {
        this.generateConsume = generateConsume;
    }

    public String getImgThumbUrl() {
        return imgThumbUrl;
    }

    public void setImgThumbUrl(String imgThumbUrl) {
        this.imgThumbUrl = imgThumbUrl;
    }

    public Boolean getFrame() {
        return frame;
    }

    public void setFrame(Boolean frame) {
        this.frame = frame;
    }
}
