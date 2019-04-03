//package com.xjysx.xiaochetong.receiver;
//
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//
//
//import com.xjysx.xiaochetong.R;
//import com.xjysx.xiaochetong.moudle.SplashActivity;
//import com.xjysx.xiaochetong.moudle.WebViewActivity;
//import com.xjysx.xiaochetong.util.NotifyUtil;
//import com.xjysx.xiaochetong.util.SharedPreferenceUtil;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Iterator;
//
//
//import static com.xjysx.xiaochetong.util.DeviceUtil.getPackageUid;
//import static com.xjysx.xiaochetong.util.DeviceUtil.isAppRunning;
//import static com.xjysx.xiaochetong.util.DeviceUtil.isProcessRunning;
//
//
///**
// * 自定义接收器
// * <p>
// * 如果不定义这个 Receiver，则：
// * 1) 默认用户会打开主界面
// * 2) 接收不到自定义消息
// */
//public class MyJPushReceiver extends BroadcastReceiver {
//    private static final String TAG = "JPush";
//    private Vibrator vibrator;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            String content = bundle.getString(JPushInterface.EXTRA_TITLE);
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: title:" + content + "message:" + message);
//            notify(context, content, message);
//            context.sendBroadcast(new Intent(WebViewActivity.MESSAGE_RECEIVED_ACTION).putExtra("data", message));
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//            context.sendBroadcast(new Intent(WebViewActivity.MESSAGE_RECEIVED_ACTION).putExtra("data", bundle.getString(JPushInterface.EXTRA_MESSAGE)));
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//            Intent i = null;
//            String jsonObject = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            try {
//                JSONObject object = new JSONObject(jsonObject);
//                if (object.has("url")) {
//                    String url = object.getString("url");
//                    String pName = context.getPackageName();
//                    int uid = getPackageUid(context, pName);
//                    if (uid > 0) {
//                        boolean rstA = isAppRunning(context, pName);
//                        boolean rstB = isProcessRunning(context, uid);
//                        if (rstA || rstB) {
//                            //指定包名的程序正在运行中
//                            i = new Intent(context, WebViewActivity.class);
//                            i.putExtra("url", url);
//                        } else {
//                            i = new Intent(context, SplashActivity.class);
//                        }
//                    } else {
//                        //应用未安装
//                    }
//                } else {
//                    i = new Intent(context, WebViewActivity.class);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                i = new Intent(context, WebViewActivity.class);
//            }
//            //打开自定义的Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
//        } else {
//            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//        }
//    }
//
//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
//                    Log.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it = json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next().toString();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    Log.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
//    }
//
//
//    private void notify(Context context, String content, String msg) {
//        String title = context.getString(R.string.app_name);
//        try {
//            JSONObject jsonObject = new JSONObject(msg);
//            if (jsonObject.has("url")) {
//                String url = jsonObject.getString("url");
//                Intent intent = null;
//                String pName = context.getPackageName();
//                int uid = getPackageUid(context, pName);
//                if (uid > 0) {
//                    boolean rstA = isAppRunning(context, pName);
//                    boolean rstB = isProcessRunning(context, uid);
//                    if (rstA || rstB) {
//                        //指定包名的程序正在运行中
//                        intent = new Intent(context, WebViewActivity.class);
//                        intent.putExtra("url", url);
//                    } else {
//                        intent = new Intent(context, SplashActivity.class);
//                    }
//                } else {
//                    //应用未安装
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent appIntent = PendingIntent.getActivity(context, 0,
//                        intent, 0);
//                NotifyUtil.notify(context, appIntent, title, content);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    //播放铃声
//    public void ringPlay(Context context) {
//        SharedPreferences sp = SharedPreferenceUtil.getSharedPreferences(context);
//        int position = sp.getInt("ring", 0);
//        /*判断位置不为0则播放的条目为position-1*/
//        if (position != 0) {
//            try {
//                RingtoneManager rm = new RingtoneManager(context);
//                rm.setType(RingtoneManager.TYPE_NOTIFICATION);
//                rm.getCursor();
//                rm.getRingtone(position - 1).play();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//            /*position为0是跟随系统，先得到系统所使用的铃声，然后播放*/
//        if (position == 0) {
//            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
//                    context, RingtoneManager.TYPE_NOTIFICATION);
//            RingtoneManager.getRingtone(context, uri).play();
//        }
//    }
//}
