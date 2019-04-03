//package com.xjysx.xiaochetong.moudle;
//
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.WindowManager;
//
//import com.xjysx.xiaochetong.R;
//import com.xjysx.xiaochetong.base.BaseActivity;
//import com.xjysx.xiaochetong.entities.AppBean;
//import com.xjysx.xiaochetong.moudle.adapter.AppAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//public class AllAppActivity extends BaseActivity {
//
//    @BindView(R.id.recycler_view)
//    RecyclerView recyclerView;
//
//    private AppAdapter appAdapter;
//    private List<AppBean> list;
//    private String version;
//    private String packageName;
//    private String appName;
//    private int resId;
//
//    @Override
//    protected void loadLayout() {
//        setContentView(R.layout.activity_app);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
//
//    @Override
//    public void initPresenter() {
//        version = getString(R.string.version);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
//        recyclerView.setLayoutManager(layoutManager);
//        list = new ArrayList<>();
////        if (version.equals("th")) {
////            packageName = "com.example.administrator.anxin_agent_tianhui";
////            appName = "天惠行销宝";
////            resId=R.mipmap.th;
////        } else if (version.equals("cbb")) {
////            packageName = "com.ysx.cxbb";
////            appName = "车保宝";
////            resId=R.mipmap.cbb;
////        } else if (version.equals("zlb")) {
////            packageName = "com.ysx.zlb";
////            appName = "中联保";
////            resId=R.mipmap.zlb;
////        } else if (version.equals("fbb")) {
////            packageName = "com.ysx.fbb";
////            appName = "富保宝";
////        } else if (version.equals("zj")) {
////            packageName = "com.ysx.zjb";
////            appName = "紫金宝";
////        } else if (version.equals("tcb")) {
////            packageName = "com.ysx.tcb";
////            appName = "太创保";
////        }
//        list.add(new AppBean("天惠行销宝",R.mipmap.th,"com.example.administrator.anxin_agent_tianhui"));
//        list.add(new AppBean("车保宝",R.mipmap.cbb,"com.ysx.cxbb"));
//        list.add(new AppBean("富保宝",R.mipmap.fbb,"com.ysx.fbb"));
//        list.add(new AppBean("紫金宝",R.mipmap.zjb,"com.ysx.zjb"));
//        list.add(new AppBean("太创保",R.mipmap.tcb,"com.ysx.tcb"));
//        list.add(new AppBean("前海创",R.mipmap.qhc,"com.ysx.qhc"));
//        list.add(new AppBean("中联保",R.mipmap.zlb,"com.ysx.zlb"));
//        list.add(new AppBean("智佳保宝雇主险",R.mipmap.employer_insurance,"com.ysx.gzx"));
//        list.add(new AppBean("银企维保",R.mipmap.logo_weibao,"com.ysx.wb"));
//        list.add(new AppBean("行销管理平台",R.mipmap.agent,"com.ysx.xxdl"));
//        list.add(new AppBean("保险监控大屏",R.mipmap.bigdata,"com.ysx.bxjk"));
//        list.add(new AppBean("行销宝",R.mipmap.market_back,"com.ysx.xxht"));
//        list.add(new AppBean("小车童Tra",R.mipmap.icon_b,"com.sjdcar.xctt"));
//        list.add(new AppBean("嘟嘟理赔",R.mipmap.ddlp,"com.example.administrator.anxincompensate"));
//        appAdapter = new AppAdapter(this);
//        appAdapter.setData(list);
//        recyclerView.setAdapter(appAdapter);
//    }
//
//
//}
