package com.xjysx.xiaochetong.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by dan on 2018/6/27/027.
 */
@Entity
public class UserBean {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String user_id;

    public UserBean(String user_id) {
        this.user_id = user_id;
        id = null;
    }

    @Generated(hash = 1021730999)
    public UserBean(Long id, String user_id) {
        this.id = id;
        this.user_id = user_id;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
