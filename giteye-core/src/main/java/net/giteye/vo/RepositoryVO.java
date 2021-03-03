package net.giteye.vo;

import net.giteye.enums.GitSite;

import java.io.Serializable;

/**
 * Git仓库VO类
 *
 * @author gongjun[dt_flys@hotmail.com]
 */
public class RepositoryVO extends BaseVO{

    /**
     * 会员CODE
     */
    private String memberCode;

    /**
     * Git站点
     */
    private GitSite gitSite;

    /**
     * Git账户名称
     */
    private String username;

    /**
     * Git仓库名称
     */
    private String repositoryName;

    /**
     * Git仓库地址
     */
    private String repositoryUrl;

    /**
     * 是否允许统计Star数
     */
    private Boolean allowCalculateStars;

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public GitSite getGitSite() {
        return gitSite;
    }

    public void setGitSite(GitSite gitSite) {
        this.gitSite = gitSite;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public Boolean getAllowCalculateStars() {
        return allowCalculateStars;
    }

    public void setAllowCalculateStars(Boolean allowCalculateStars) {
        this.allowCalculateStars = allowCalculateStars;
    }
}
