package com.xjysx.xiaochetong.entities;

/**
 * Created by dan on 2018/7/6/006.
 */

public class PayTypeBean {
    int wechat;
    int alipay;

    public int getWechat() {
        return wechat;
    }

    public void setWechat(int wechat) {
        this.wechat = wechat;
    }

    public int getAlipay() {
        return alipay;
    }

    public void setAlipay(int alipay) {
        this.alipay = alipay;
    }

    public PayTypeBean(int wechat, int alipay) {
        this.wechat = wechat;
        this.alipay = alipay;
    }

    public PayTypeBean() {
    }
}
