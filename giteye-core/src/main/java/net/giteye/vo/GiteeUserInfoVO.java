package net.giteye.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 用户信息实体类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class GiteeUserInfoVO extends BaseVO{

    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "login")
    private String login;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "avatar_url")
    private String avatarUrl;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "html_url")
    private String htmlUrl;

    @JSONField(name = "followers_url")
    private String followersUrl;

    @JSONField(name = "following_url")
    private String followingUrl;

    @JSONField(name = "gists_url")
    private String gistsUrl;

    @JSONField(name = "starred_url")
    private String starredUrl;

    @JSONField(name = "subscriptions_url")
    private String subscriptionsUrl;

    @JSONField(name = "organizations_url")
    private String organizationsUrl;

    @JSONField(name = "repos_url")
    private String reposUrl;

    @JSONField(name = "events_url")
    private String eventsUrl;

    @JSONField(name = "received_events_url")
    private String receivedEventsUrl;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "blog")
    private String blog;

    @JSONField(name = "weibo")
    private String weibo;

    @JSONField(name = "bio")
    private String bio;

    @JSONField(name = "public_repos")
    private Integer publicRepos;

    @JSONField(name = "public_gists")
    private Integer publicGists;

    @JSONField(name = "followers")
    private Integer followers;

    @JSONField(name = "following")
    private Integer following;

    @JSONField(name = "stared")
    private Integer stared;

    @JSONField(name = "watched")
    private Integer watched;

    @JSONField(name = "created_at")
    private Date createdAt;

    @JSONField(name = "updated_at")
    private Date updatedAt;

    @JSONField(name = "email")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public void setGistsUrl(String gistsUrl) {
        this.gistsUrl = gistsUrl;
    }

    public String getStarredUrl() {
        return starredUrl;
    }

    public void setStarredUrl(String starredUrl) {
        this.starredUrl = starredUrl;
    }

    public String getSubscriptionsUrl() {
        return subscriptionsUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public String getOrganizationsUrl() {
        return organizationsUrl;
    }

    public void setOrganizationsUrl(String organizationsUrl) {
        this.organizationsUrl = organizationsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getReceivedEventsUrl() {
        return receivedEventsUrl;
    }

    public void setReceivedEventsUrl(String receivedEventsUrl) {
        this.receivedEventsUrl = receivedEventsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(Integer publicRepos) {
        this.publicRepos = publicRepos;
    }

    public Integer getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(Integer publicGists) {
        this.publicGists = publicGists;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getStared() {
        return stared;
    }

    public void setStared(Integer stared) {
        this.stared = stared;
    }

    public Integer getWatched() {
        return watched;
    }

    public void setWatched(Integer watched) {
        this.watched = watched;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
