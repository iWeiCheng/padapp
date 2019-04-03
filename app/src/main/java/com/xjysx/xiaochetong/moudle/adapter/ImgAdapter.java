package com.xjysx.xiaochetong.moudle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.moudle.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 适配器
 * Created by danjj on 2017/2/17.
 */
public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.MyViewHolder> {

    private BaseActivity activity;
    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public ImgAdapter(BaseActivity activity) {
        this.activity = activity;
        list = new ArrayList<>();
    }

    public void addData(List<Integer> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<Integer> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                activity).inflate(R.layout.item_image, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int item = list.get(position);
        holder.item_img.setImageResource(item);
        holder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == list.size() - 1) {
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_img)
        ImageView item_img;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
