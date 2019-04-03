package com.xjysx.xiaochetong.entities;

/**
 * Created by dan on 2018/6/28/028.
 */

public class FingerResultBean {
    FingerLoginBean data;
    int code;

    public FingerLoginBean getFingerLoginBean() {
        return data;
    }

    public void setFingerLoginBean(FingerLoginBean fingerLoginBean) {
        this.data = fingerLoginBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public FingerResultBean(FingerLoginBean fingerLoginBean, int code) {

        this.data = fingerLoginBean;
        this.code = code;
    }

    public FingerResultBean() {
    }
}
