package com.xjysx.xiaochetong.base;

import android.content.Context;

import com.xjysx.xiaochetong.R;


/**
 * http访问网络时的对话框
 */
public class CommonProgressDialog extends BaseDialog {

    public CommonProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.common_dialog_progress_default);
    }
}