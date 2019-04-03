package com.xjysx.xiaochetong.moudle;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BottomDialogFragment;

import butterknife.BindView;

/**
 * Created by danjj on 2016/12/10.
 */
public class ShareFragment extends BottomDialogFragment implements View.OnClickListener {

//    @BindView(R.id.share_layout)
//    LinearLayout shareLayout;

    private OnClickShareListener listener;


    public static ShareFragment getInstance() {
        ShareFragment f = new ShareFragment();
        return f;
    }

    public void setListener(OnClickShareListener listener) {
        this.listener = listener;
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.share);
    }


    @Override
    protected void setListener(View view, Bundle savedInstanceState) {
        super.setListener(view, savedInstanceState);
//        for (int i = 0; i < shareLayout.getChildCount(); i++) {
//            shareLayout.getChildAt(i).setTag(i);
//            shareLayout.getChildAt(i).setOnClickListener(this);
//        }
//        for (int i = 0; i < shareLayout2.getChildCount(); i++) {
//            shareLayout2.getChildAt(i).setTag(i);
//            shareLayout2.getChildAt(i).setOnClickListener(this);
//        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
//        if (v.getParent() == shareLayout) {
//            if (listener != null)
//                listener.onClickShare(position);
//        }
//        else if (v.getParent() == shareLayout2) {
//            if (listener != null)
//                listener.onClickShare(position + 4);
//        }
    }

    public interface OnClickShareListener {

        void onClickShare(int position);
    }
}
