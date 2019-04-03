package com.xjysx.xiaochetong.datebase;

import android.content.Context;


import com.xjysx.xiaochetong.entities.UserBean;
import com.xjysx.xiaochetong.greendao.DaoMaster;
import com.xjysx.xiaochetong.greendao.DaoSession;
import com.xjysx.xiaochetong.greendao.UserBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


public class UserDao {
    private DBManager mDBManager;

    public UserDao(Context context) {
        mDBManager = DBManager.getInstance(context);
    }

    //增删改查---------------------------------------

    /**
     * 插入一条
     *
     * @param userBean
     */
    public void insertTestEntities(UserBean userBean) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao testEntityDao = daoSession.getUserBeanDao();
        testEntityDao.insert(userBean);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertTestEntityList(List<UserBean> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        userDao.insertOrReplaceInTx(users);
    }

    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteUser(UserBean user) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        userDao.delete(user);
    }

    /**
     * 删除用户记录
     *
     * @param user_id
     */
    public void deleteUser(String user_id) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        QueryBuilder<UserBean> qb = userDao.queryBuilder();
        List<UserBean> list = qb.where(UserBeanDao.Properties.User_id.eq(user_id)).list();
        for (UserBean userBean : list) {
            deleteUser(userBean);
        }
    }


    /**
     * 删除所有记录
     */
    public void deleteAllTestEntity() {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao categoryEntityDao = daoSession.getUserBeanDao();
        categoryEntityDao.deleteAll();
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateTestEntity(UserBean user) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        userDao.update(user);
    }

    /**
     * 更新多条记录
     *
     * @param user
     */
    public void updateAllTestEntity(List<UserBean> user) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        userDao.updateInTx(user);
    }

    /**
     * 查询用户列表
     */
    public List<UserBean> queryUserEntityList() {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        QueryBuilder<UserBean> qb = userDao.queryBuilder();
        List<UserBean> list = qb.orderAsc(UserBeanDao.Properties.Id).list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<UserBean> queryUserEntityList(String user_id) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        QueryBuilder<UserBean> qb = userDao.queryBuilder();
        List<UserBean> list = qb.where(UserBeanDao.Properties.User_id.eq(user_id))
                .orderAsc(UserBeanDao.Properties.Id).list();
        return list;
    }


    /**
     * 查询 某一个名称的 的位置
     */
    public long queryMoreTestEntity(String user_id) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        QueryBuilder<UserBean> qb = userDao.queryBuilder();
        UserBean UserBean = qb.where(UserBeanDao.Properties.User_id.eq(user_id)).uniqueOrThrow();
        return UserBean.getId();
    }


    /**
     * 更新 某一个名称 一条记录的位置为第六个
     */
    public void updateMoreTestEntity(String user_id) {
        DaoMaster daoMaster = new DaoMaster(mDBManager.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserBeanDao userDao = daoSession.getUserBeanDao();
        QueryBuilder<UserBean> qb = userDao.queryBuilder();
        UserBean userBean = qb.where(UserBeanDao.Properties.User_id.eq(user_id)).uniqueOrThrow();
        userBean.setUser_id(user_id);
        userDao.update(userBean);
    }
}
