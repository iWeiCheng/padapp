package com.xjysx.xiaochetong.entities;


/**
 * Created by dan on 2018/6/27/027.
 */
public class FingerLoginBean {

    String userId;
    String random;
    String sign;

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
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



    public FingerLoginBean(String userId, String random, String sign) {
        this.userId = userId;
        this.random = random;
        this.sign = sign;
    }

    public FingerLoginBean() {
    }


}
