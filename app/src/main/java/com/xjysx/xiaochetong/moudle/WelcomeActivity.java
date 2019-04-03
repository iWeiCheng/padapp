package com.xjysx.xiaochetong.moudle;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.WindowManager;

import com.caijia.widget.looperrecyclerview.LooperPageRecyclerView;
import com.caijia.widget.looperrecyclerview.RecyclerViewCircleIndicator;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.SPUtils;
import com.xjysx.xiaochetong.app.Const;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.moudle.adapter.ImgAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 欢迎页面
 * Created by dan on 2018/6/21/021.
 */

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.looper_page_recycler_view)
    LooperPageRecyclerView looperPageRecyclerView;
    @BindView(R.id.indicator)
    RecyclerViewCircleIndicator indicator;


    private ImgAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void loadLayout() {
//        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 加上这句设置为全屏 不加则只隐藏title
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    protected int getTranslucentColor() {
        return R.color.transparent;
    }

    @Override
    public void initPresenter() {
        SPUtils.put(this, Const.FIRST_OPEN, false);
        looperPageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImgAdapter(this);
        List<Integer> list = new ArrayList<>();
//        if (getString(R.string.type).equals("b")) {
//            list.add(R.mipmap.b1);
//            list.add(R.mipmap.b2);
//            list.add(R.mipmap.b3);
//        } else if (getString(R.string.type).equals("c")) {
//            list.add(R.mipmap.c1);
//            list.add(R.mipmap.c2);
//            list.add(R.mipmap.c3);
//        }
        adapter.setData(list);
        looperPageRecyclerView.setAdapter(adapter);
        indicator.setupWithRecyclerView(looperPageRecyclerView);
    }


}
