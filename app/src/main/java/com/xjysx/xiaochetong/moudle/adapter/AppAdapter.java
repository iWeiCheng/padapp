package com.xjysx.xiaochetong.moudle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.entities.AppBean;
import com.xjysx.xiaochetong.moudle.WebViewActivity;
import com.xjysx.xiaochetong.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 适配器
 * Created by danjj on 2017/2/17.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {

    private BaseActivity activity;
    private List<AppBean> list;

    public List<AppBean> getList() {
        return list;
    }

    public AppAdapter(BaseActivity activity) {
        this.activity = activity;
        list = new ArrayList<>();
    }

    public void addData(List<AppBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<AppBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                activity).inflate(R.layout.item_app, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AppBean item = list.get(position);
        holder.item_icon.setImageResource(item.getResId());
        holder.item_name.setText(item.getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtil.checkPackInfo(activity, item.getPackageName())) {
                    DeviceUtil.openPackage(activity, item.getPackageName());
                } else {
                    Toast.makeText(activity, "您还没有安装" + item.getName() + ",请先安装后再使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_icon)
        ImageView item_icon;
        @BindView(R.id.item_name)
        TextView item_name;
        @BindView(R.id.root)
        View root;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
