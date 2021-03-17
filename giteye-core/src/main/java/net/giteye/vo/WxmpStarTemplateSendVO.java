package net.giteye.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;

/**
 * 微信公众号模板发送VO
 *
 * @author Bryan.Zhang
 * @since 2021/3/11
 */
public class WxmpStarTemplateSendVO extends BaseVO {

    private String userOpenId;

    private String userName;

    private String gitUserName;

    private String repo;

    private String gitSite;

    private Date time;

    private int incrementStarCnt;

    private int totalStarCnt;

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getGitUserName() {
        return gitUserName;
    }

    public void setGitUserName(String gitUserName) {
        this.gitUserName = gitUserName;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getGitSite() {
        return gitSite;
    }

    public void setGitSite(String gitSite) {
        this.gitSite = gitSite;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getIncrementStarCnt() {
        return incrementStarCnt;
    }

    public void setIncrementStarCnt(int incrementStarCnt) {
        this.incrementStarCnt = incrementStarCnt;
    }

    public int getTotalStarCnt() {
        return totalStarCnt;
    }

    public void setTotalStarCnt(int totalStarCnt) {
        this.totalStarCnt = totalStarCnt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStr() {
        return DateUtil.format(this.time, DatePattern.NORM_DATETIME_PATTERN);
    }

    public String getContent() {
        return StrUtil.format("你在{}上的仓库[{}/{}]已达到{}个Star！新增了{}个Star。",
                gitSite, gitUserName, repo, totalStarCnt, incrementStarCnt);
    }
}
