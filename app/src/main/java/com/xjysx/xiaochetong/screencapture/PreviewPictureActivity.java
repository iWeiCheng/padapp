package com.xjysx.xiaochetong.screencapture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.app.BaseApplication;

/**
 *
 */
public class PreviewPictureActivity extends FragmentActivity implements GlobalScreenshot.onScreenShotListener {

  public static final Intent newIntent(Context context) {
    Intent intent = new Intent(context, PreviewPictureActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return intent;
  }

  private ImageView mPreviewImageView;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_preview_layout);
    mPreviewImageView = (ImageView) findViewById(R.id.preview_image);

    GlobalScreenshot screenshot = new GlobalScreenshot(getApplicationContext());

    Bitmap bitmap = ((BaseApplication) getApplication()).getmScreenCaptureBitmap();


    Log.e("ryze", "预览图片");
    mPreviewImageView.setImageBitmap(bitmap);
    mPreviewImageView.setVisibility(View.GONE);

    if (bitmap != null) {
      screenshot.takeScreenshot(bitmap, this, true, true);
    }

  }

  @Override
  public void onStartShot() {

  }

  @Override
  public void onFinishShot(boolean success) {
    mPreviewImageView.setVisibility(View.VISIBLE);
  }
}
