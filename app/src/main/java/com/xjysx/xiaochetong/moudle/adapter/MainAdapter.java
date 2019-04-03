//package com.xjysx.xiaochetong.moudle.adapter;
//
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.xjysx.xiaochetong.R;
//import com.xjysx.xiaochetong.base.BaseActivity;
//import com.xjysx.xiaochetong.moudle.WebViewActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * 适配器
// * Created by danjj on 2017/2/17.
// */
//public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
//
//    private BaseActivity activity;
//    private List<Integer> list;
//
//    public List<Integer> getList() {
//        return list;
//    }
//
//    public MainAdapter(BaseActivity activity) {
//        this.activity = activity;
//        list = new ArrayList<>();
//    }
//
//    public void addData(List<Integer> list) {
//        this.list.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    public void setData(List<Integer> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
//                activity).inflate(R.layout.item_main, parent,
//                false));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        ViewGroup.LayoutParams layoutParams = holder.root.getLayoutParams();
//        layoutParams.width = (ScreenUtils.getScreenWidth() - DpPxUtil.dp2px(getActivity(), 70)) / 3;//
//        holder.getView(R.id.ll_root).setLayoutParams(layoutParams);
//
//        int item = list.get(position);
//        holder.item_img.setImageResource(item);
//        holder.item_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (position == list.size() - 1) {
////                    Intent intent = new Intent(activity, WebViewActivity.class);
////                    activity.startActivity(intent);
////                    activity.finish();
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.item_img)
//        ImageView item_img;
//        @BindView(R.id.root)
//        View root;
//
//        public MyViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//}
