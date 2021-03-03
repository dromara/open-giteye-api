package net.giteye.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 * 
 * </p>
 *
 * @author Bryan.Zhang
 * @since 2021-01-20
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String wxOpenId;

    private String wxUnionId;

    private String wxNickName;

    private Integer sex;

    private String language;

    private String city;

    private String country;

    private String mobile;

    private String headImgUrl;

    private Date wxSubscribeTime;

    private String subscribeScene;

    private String qrScene;

    private Date updateTime;

    private Integer isSubscribe;

    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getWxSubscribeTime() {
        return wxSubscribeTime;
    }

    public void setWxSubscribeTime(Date wxSubscribeTime) {
        this.wxSubscribeTime = wxSubscribeTime;
    }

    public String getSubscribeScene() {
        return subscribeScene;
    }

    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    public String getQrScene() {
        return qrScene;
    }

    public void setQrScene(String qrScene) {
        this.qrScene = qrScene;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setCreateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserInfo.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("wxOpenId='" + wxOpenId + "'")
                .add("wxUnionId='" + wxUnionId + "'")
                .add("wxNickName='" + wxNickName + "'")
                .add("sex=" + sex)
                .add("language='" + language + "'")
                .add("city='" + city + "'")
                .add("country='" + country + "'")
                .add("mobile='" + mobile + "'")
                .add("headImgUrl='" + headImgUrl + "'")
                .add("wxSubscribeTime=" + wxSubscribeTime)
                .add("subscribeScene='" + subscribeScene + "'")
                .add("qrScene='" + qrScene + "'")
                .add("updateTime=" + updateTime)
                .add("isSubscribe=" + isSubscribe)
                .add("status=" + status)
                .toString();
    }
}
