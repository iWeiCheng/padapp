package com.xjysx.xiaochetong.entities;

/**
 * 版本更新
 * Created by dan on 2017/12/6/006.
 */

public class VersionBean {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 版本号
     */
    private String versionNumber;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 版本下载地址
     */
    private String versionUrl;

    /**
     * app类型：Android-1004 IOS-1003
     */
    private String appType;

    /**
     * 是否强制更新
     */
    private String updateFlag;

    /**
     * 提示、说明信息
     */
    private String message;

    /**
     * 随机数
     */
    private String random;

    /**
     * 签名
     */
    private String sign;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 渠道
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
