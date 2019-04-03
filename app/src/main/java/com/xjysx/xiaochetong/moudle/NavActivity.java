package com.xjysx.xiaochetong.moudle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class NavActivity extends BaseActivity {
//    @BindView(R.id.ll_url)
//    LinearLayout llUrl;

    @Override
    protected int getTranslucentColor() {
        return R.color.orange;
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_nav);
    }

    @Override
    public void initPresenter() {

    }

//
//    @OnClick(R.id.ll_url)
//    public void onViewClicked() {
//        Intent intent = new Intent(this,WebViewActivity.class);
//        startActivity(intent);
//    }
}
