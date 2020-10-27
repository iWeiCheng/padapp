package com.xjysx.xiaochetong.screencapture;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.accessibility.HongBaoService;
import com.xjysx.xiaochetong.floatwindow.FloatWindowManager;

public class MainActivity extends FragmentActivity {


  public static final int REQUEST_MEDIA_PROJECTION = 18;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
    requestCapturePermission();
  }

  @Override
  protected void onResume() {
    super.onResume();
//    if (!HongBaoService.isStart()) {
//      try {
//        this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//      } catch (Exception e) {
//        this.startActivity(new Intent(Settings.ACTION_SETTINGS));
//        e.printStackTrace();
//      }
//    }
  }

  public void requestCapturePermission() {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      //5.0 之后才允许使用屏幕截图

      return;
    }

    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
        getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    startActivityForResult(
        mediaProjectionManager.createScreenCaptureIntent(),
        REQUEST_MEDIA_PROJECTION);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_MEDIA_PROJECTION:

        if (resultCode == RESULT_OK && data != null) {
          FloatWindowsService.setResultData(data);
          startService(new Intent(getApplicationContext(), FloatWindowsService.class));
        }
        break;
    }

  }

}
