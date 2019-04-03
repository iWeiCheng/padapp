package com.xjysx.xiaochetong.network;


import org.greenrobot.eventbus.EventBus;

import rx.Observer;

/**
 * 自定义封装
 * Created by dan on 2017/8/16.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean> {


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        networkError(e);
    }

    @Override
    public void onNext(BaseBean baseBean) {
        if (baseBean.getResultCode()==0) {//成功
            onSuccess((T) baseBean.getData());
        } else {
            onError(0, "", baseBean);
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onError(int code, String message, BaseBean baseBean);

    public abstract void networkError(Throwable e);
}
