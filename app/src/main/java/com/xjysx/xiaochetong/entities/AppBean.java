package com.xjysx.xiaochetong.entities;

public class AppBean {
    private String name;
    private int resId;
    private String packageName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AppBean(String name, int resId, String packageName) {
        this.name = name;
        this.resId = resId;
        this.packageName = packageName;
    }

    public AppBean() {
    }

    public int getResId() {
        return resId;

    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
