package com.xjysx.xiaochetong.moudle;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Preconditions;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.entities.MainBean;
import com.xjysx.xiaochetong.network.BaseBean;
import com.xjysx.xiaochetong.network.BaseObserver;
import com.xjysx.xiaochetong.network.Network;
import com.xjysx.xiaochetong.util.DeviceUtil;
import com.xjysx.xiaochetong.util.RandomNum;
import com.xjysx.xiaochetong.util.SignatureUtil;
import com.xjysx.xiaochetong.util.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {


    @BindView(R.id.iv_icon1)
    ImageView ivIcon1;
    @BindView(R.id.tv_app1)
    TextView tvApp1;
    @BindView(R.id.tv_detail_1)
    TextView tvDetail1;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.iv_icon2)
    ImageView ivIcon2;
    @BindView(R.id.tv_app2)
    TextView tvApp2;
    @BindView(R.id.tv_detail_2)
    TextView tvDetail2;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.iv_icon3)
    ImageView ivIcon3;
    @BindView(R.id.tv_app3)
    TextView tvApp3;
    @BindView(R.id.tv_detail_3)
    TextView tvDetail3;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.iv_icon4)
    ImageView ivIcon4;
    @BindView(R.id.tv_app4)
    TextView tvApp4;
    @BindView(R.id.tv_detail_4)
    TextView tvDetail4;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;
    @BindView(R.id.iv_icon5)
    ImageView ivIcon5;
    @BindView(R.id.tv_app5)
    TextView tvApp5;
    @BindView(R.id.tv_detail_5)
    TextView tvDetail5;
    @BindView(R.id.rl_5)
    RelativeLayout rl5;
    @BindView(R.id.iv_icon6)
    ImageView ivIcon6;
    @BindView(R.id.tv_app6)
    TextView tvApp6;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rl_6)
    RelativeLayout rl6;
    private String packageName;
    private String appName;
    private MainActivity mainActivity;

    private Subscription mSubscription;
    private MainBean mainBean;

    private BaseObserver<MainBean> observer = new BaseObserver<MainBean>() {

        @Override
        public void onSuccess(MainBean bean) {
            dismissDialog();
            mainBean = bean;
            setData();
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            dismissDialog();
            ToastUtil.showToast(getContext(), message);
        }

        @Override
        public void networkError(Throwable e) {
            dismissDialog();
            ToastUtil.showToast(getContext(), getContext().getResources().getString(R.string.network_error));
        }
    };
    private String TAG="MainActivity";

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initPresenter() {
        mainActivity = this;
        unsubscribe();
        getMainData();
    }


    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private void getMainData() {
        showProgressDialog();
        String str_random = RandomNum.getrandom();
        Map<String, String> map = new HashMap<>();
        map.put("method", "homeInterface");
        map.put("platform", "1");
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);

        mSubscription = Network.getMainData().getMainData(Network.WORK_URL, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    private void setData() {
        if (mainBean == null) {
            return;
        }
        tvApp1.setText(mainBean.getDataList().get(0).getName());
        tvApp2.setText(mainBean.getDataList().get(1).getName());
        tvApp3.setText(mainBean.getDataList().get(2).getName());
        tvApp4.setText(mainBean.getDataList().get(3).getName());
        tvApp5.setText(mainBean.getDataList().get(4).getName());
        tvApp6.setText(mainBean.getDataList().get(5).getName());
        tvDetail1.setText(mainBean.getDataList().get(0).getDescribe());
        tvDetail2.setText(mainBean.getDataList().get(1).getDescribe());
        tvDetail3.setText(mainBean.getDataList().get(2).getDescribe());
        tvDetail4.setText(mainBean.getDataList().get(3).getDescribe());
        tvDetail5.setText(mainBean.getDataList().get(4).getDescribe());
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(ivIcon1);
        imageViews.add(ivIcon2);
        imageViews.add(ivIcon3);
        imageViews.add(ivIcon4);
        imageViews.add(ivIcon5);
        imageViews.add(ivIcon6);
        for (int i = 0; i < mainBean.getDataList().size(); i++) {
            setIcon(mainBean.getDataList().get(i).getIcon(), imageViews.get(i));
        }

    }

    private void setIcon(final String url, final ImageView iv) {
        if (url.equals("")) {
            iv.setVisibility(View.GONE);
        } else if (url.endsWith(".svg")) {//是svg图片
            GlideToVectorYou.justLoadImage(mainActivity, Uri.parse(url), iv);
        } else {
            Glide.with(this)
                    .load(url)
                    .into(iv);
        }
    }



    @OnClick({R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.rl_6,R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                if (mainBean == null) {
                    return;
                }
                clickEvent(0);
                break;
            case R.id.rl_2:
                if (mainBean == null) {
                    return;
                }
                clickEvent(1);
                break;
            case R.id.rl_3:
                if (mainBean == null) {
                    return;
                }
                clickEvent(2);
                break;
            case R.id.rl_4:
                if (mainBean == null) {
                    return;
                }
                clickEvent(3);
                break;
            case R.id.rl_5:
                if (mainBean == null) {
                    return;
                }
                clickEvent(4);
                break;
            case R.id.rl_6:
                if (mainBean == null) {
                    return;
                }
                clickEvent(5);
                break;
            case R.id.tv_more:
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
        }
    }

    /**
     *
     * @param i  位置
     */
    private void clickEvent(int i) {
        if(mainBean.getDataList().get(i).getType().equals("1")){//1App，2网页
            packageName = mainBean.getDataList().get(i).getApplication();
            appName = mainBean.getDataList().get(i).getName();
            if (DeviceUtil.checkPackInfo(this, packageName)) {
                DeviceUtil.openPackage(this, packageName);
            } else {
                Toast.makeText(this, "您还没有安装" + appName + ",请先安装后再使用", Toast.LENGTH_SHORT).show();
            }
        }else if(mainBean.getDataList().get(i).getType().equals("2")){
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("url",mainBean.getDataList().get(i).getApplication());
            startActivity(intent);
        }
    }

}
