package net.giteye.vo;

import java.util.Date;

public class ChartDataVO extends BaseVO{

    private Long id;

    private Long dataRecordId;

    private Long userId;

    private String gitSite;

    private String owner;

    private String repo;

    private String metricsType;

    private String xValue;

    private Integer yValue;

    private String extData;

    private Date createDate;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getMetricsType() {
        return metricsType;
    }

    public void setMetricsType(String metricsType) {
        this.metricsType = metricsType;
    }

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public Integer getyValue() {
        return yValue;
    }

    public void setyValue(Integer yValue) {
        this.yValue = yValue;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getDataRecordId() {
        return dataRecordId;
    }

    public void setDataRecordId(Long dataRecordId) {
        this.dataRecordId = dataRecordId;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }else {
            if(getClass() != obj.getClass()) {
                return false;
            }else {
                if(((ChartDataVO)obj).getDataRecordId().equals(this.getDataRecordId())
                        && ((ChartDataVO)obj).getUserId().equals(this.getUserId())
                        && ((ChartDataVO)obj).getGitSite().equals(this.getGitSite())
                        && ((ChartDataVO)obj).getOwner().equals(this.getOwner())
                        && ((ChartDataVO)obj).getRepo().equals(this.getRepo())
                        && ((ChartDataVO)obj).getMetricsType().equals(this.getMetricsType())
                        && ((ChartDataVO)obj).getxValue().equals(this.getxValue())) {
                    return true;
                }else {
                    return false;
                }
            }
        }
    }
}
