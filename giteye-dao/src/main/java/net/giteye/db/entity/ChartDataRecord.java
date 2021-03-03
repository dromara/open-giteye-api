package net.giteye.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Bryan.Zhang
 * @since 2021-02-20
 */
public class ChartDataRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String gitSite;

    private String gitUsername;

    private String repoName;

    private String metricsType;

    private Boolean supportIncrement;

    private Integer lastDataPage;

    private Integer lastDataIndex;

    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMetricsType() {
        return metricsType;
    }

    public void setMetricsType(String metricsType) {
        this.metricsType = metricsType;
    }

    public Boolean getSupportIncrement() {
        return supportIncrement;
    }

    public void setSupportIncrement(Boolean supportIncrement) {
        this.supportIncrement = supportIncrement;
    }

    public Integer getLastDataPage() {
        return lastDataPage;
    }

    public void setLastDataPage(Integer lastDataPage) {
        this.lastDataPage = lastDataPage;
    }

    public Integer getLastDataIndex() {
        return lastDataIndex;
    }

    public void setLastDataIndex(Integer lastDataIndex) {
        this.lastDataIndex = lastDataIndex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChartDataRecord{" +
        "id=" + id +
        ", userId=" + userId +
        ", gitSite=" + gitSite +
        ", gitUsername=" + gitUsername +
        ", repoName=" + repoName +
        ", metricsType=" + metricsType +
        ", supportIncrement=" + supportIncrement +
        ", lastDataPage=" + lastDataPage +
        ", lastDataIndex=" + lastDataIndex +
        ", createTime=" + createTime +
        "}";
    }
}
