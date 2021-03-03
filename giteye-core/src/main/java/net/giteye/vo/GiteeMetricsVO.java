package net.giteye.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class GiteeMetricsVO extends BaseVO{

    private Long repoId;

    private String owner;

    private String repoName;

    private String fullName;

    private String description;

    private String htmlUrl;

    private String sshUrl;

    @JSONField(name = "vitality")
    private int vitality;

    @JSONField(name = "vitality_percent")
    private double vitalityPercent;

    @JSONField(name = "community")
    private int community;

    @JSONField(name = "community_percent")
    private double communityPercent;

    @JSONField(name = "health")
    private int health;

    @JSONField(name = "health_percent")
    private double healthPercent;

    @JSONField(name = "trend")
    private int trend;

    @JSONField(name = "trend_percent")
    private double trendPercent;

    @JSONField(name = "influence")
    private int influence;

    @JSONField(name = "influence_percent")
    private double influencePercent;

    @JSONField(name = "total_score")
    private int totalScore;

    @JSONField(name = "created_at")
    private Date createdAt;

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public double getVitalityPercent() {
        return vitalityPercent;
    }

    public void setVitalityPercent(double vitalityPercent) {
        this.vitalityPercent = vitalityPercent;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public double getCommunityPercent() {
        return communityPercent;
    }

    public void setCommunityPercent(double communityPercent) {
        this.communityPercent = communityPercent;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getHealthPercent() {
        return healthPercent;
    }

    public void setHealthPercent(double healthPercent) {
        this.healthPercent = healthPercent;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public double getTrendPercent() {
        return trendPercent;
    }

    public void setTrendPercent(double trendPercent) {
        this.trendPercent = trendPercent;
    }

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    public double getInfluencePercent() {
        return influencePercent;
    }

    public void setInfluencePercent(double influencePercent) {
        this.influencePercent = influencePercent;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
