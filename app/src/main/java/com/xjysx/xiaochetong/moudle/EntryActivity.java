package com.xjysx.xiaochetong.moudle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.util.FrameAnimationDrawable;

import butterknife.BindView;
import butterknife.OnClick;

public class EntryActivity extends BaseActivity {


    @BindView(R.id.iv_anim)
    ImageView ivAnim;
    @BindView(R.id.tv_entry1)
    TextView tvEntry1;
    @BindView(R.id.tv_entry2)
    TextView tvEntry2;
    @BindView(R.id.tv_entry)
    TextView tvEntry;


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_entry);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void initPresenter() {

        //执行动画
        FrameAnimationDrawable.create().animateRawManuallyFromXML(R.drawable.splash, ivAnim);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止动画
        FrameAnimationDrawable.create().stopAnimation(true);
    }


    @OnClick({R.id.tv_entry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_entry:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
