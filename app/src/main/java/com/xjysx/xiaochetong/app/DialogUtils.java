package com.xjysx.xiaochetong.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class DialogUtils {

    /**
     * 显示提示消息对话框（AlertDialog）
     *
     * @param context
     * @param messageRes   提示的内容
     * @param titleResid     对话框标题id。为0 不设置标题
     * @param positiveTextid PositiveButton文字资源id
     * @param negativeTextid NegativeButton文字资源id
     * @param clickListener  按钮点击事件监听
     * @return void
     */
    public static void showAlert(Context context, String messageRes,
                                 int titleResid, int positiveTextid, int negativeTextid,
                                 DialogInterface.OnClickListener clickListener, DialogInterface.OnCancelListener onCancelListener) {
        Builder builder = new Builder(context);
        builder.setCancelable(true);
        builder.setMessage(messageRes);
        if (titleResid > 0) {
            builder.setTitle(titleResid);
        }
        builder.setPositiveButton(positiveTextid, clickListener);
        builder.setNegativeButton(negativeTextid, clickListener);
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        builder.create().show();
    }

    /**
     * 显示提示消息对话框（AlertDialog）
     *
     * @param context
     * @param message        提示的内容
     * @param titleResid     对话框标题id.为0不设置标题
     * @param positiveTextid PositiveButton文字资源id
     * @param negativeTextid NegativeButton文字资源id
     * @param clickListener  按钮点击事件监听
     * @return void
     */
    public static void showAlert(Context context, String message,
                                 int titleResid, int positiveTextid, int negativeTextid,
                                 DialogInterface.OnClickListener clickListener) {
        Builder builder = new Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        if (titleResid != 0) {
            builder.setTitle(titleResid);
        }
        if (positiveTextid != 0) {
            builder.setPositiveButton(positiveTextid, clickListener);
        }
        if (negativeTextid != 0) {
            builder.setNegativeButton(negativeTextid, clickListener);
        }
        builder.create().show();
    }

    public static void showAlert(Context context, String message,
                                 int titleResid, int positiveTextid,
                                 int middleTextid, int negativeTextid,
                                 DialogInterface.OnClickListener clickListener, DialogInterface.OnCancelListener onCancelListener) {
        Builder builder = new Builder(context);
        builder.setCancelable(false);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (titleResid != 0) {
            builder.setTitle(titleResid);
        }
        if (positiveTextid != 0) {
            builder.setPositiveButton(positiveTextid, clickListener);
        }

        if (middleTextid != 0) {
            builder.setNeutralButton(middleTextid, clickListener);
        }

        if (negativeTextid != 0) {
            builder.setNegativeButton(negativeTextid, clickListener);
        }
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnCancelListener(onCancelListener);
        alertDialog.show();
    }
}
