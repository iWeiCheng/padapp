package com.xjysx.xiaochetong.accessibility;

import android.widget.Toast;

import com.xjysx.xiaochetong.app.BaseApplication;

import java.util.Collection;

public class Utils {

    public static void toast(CharSequence cs) {
        Toast.makeText(BaseApplication.getInstance(), cs, Toast.LENGTH_SHORT).show();
    }

    //集合是否是空的
    public static boolean isEmptyArray(Collection list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmptyArray(T[] list) {
        return list == null || list.length == 0;
    }
}
