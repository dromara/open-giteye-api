package net.giteye.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class ContributorVO extends BaseVO{

    @JSONField(name = "login")
    private String login;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "avatar_url")
    private String avatarUrl;

    @JSONField(name = "html_url")
    private String htmlUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
