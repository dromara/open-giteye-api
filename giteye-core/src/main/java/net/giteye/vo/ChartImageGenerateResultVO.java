package net.giteye.vo;

import java.io.Serializable;
import java.util.Date;

public class ChartImageGenerateResultVO extends BaseVO{


    private String theme;

    private String imgUUID;

    private String imgUrl;

    private String imgType;

    private String imgFileName;

    private String thumbnailUrl;

    private Boolean success;

    private String msg;

    private Date generateStartTime;

    private Date generateEndTime;

    private Date uploadStartTime;

    private Date uploadEndTime;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImgUUID() {
        return imgUUID;
    }

    public void setImgUUID(String imgUUID) {
        this.imgUUID = imgUUID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getGenerateStartTime() {
        return generateStartTime;
    }

    public void setGenerateStartTime(Date generateStartTime) {
        this.generateStartTime = generateStartTime;
    }

    public Date getGenerateEndTime() {
        return generateEndTime;
    }

    public void setGenerateEndTime(Date generateEndTime) {
        this.generateEndTime = generateEndTime;
    }

    public Date getUploadStartTime() {
        return uploadStartTime;
    }

    public void setUploadStartTime(Date uploadStartTime) {
        this.uploadStartTime = uploadStartTime;
    }

    public Date getUploadEndTime() {
        return uploadEndTime;
    }

    public void setUploadEndTime(Date uploadEndTime) {
        this.uploadEndTime = uploadEndTime;
    }
}
