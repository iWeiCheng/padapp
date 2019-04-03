package com.xjysx.xiaochetong.moudle;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.entities.LoginBean;
import com.xjysx.xiaochetong.network.BaseBean;
import com.xjysx.xiaochetong.network.BaseObserver;
import com.xjysx.xiaochetong.network.Network;
import com.xjysx.xiaochetong.util.RandomNum;
import com.xjysx.xiaochetong.util.SignatureUtil;
import com.xjysx.xiaochetong.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    private Subscription mSubscription;

    private BaseObserver<LoginBean> observer = new BaseObserver<LoginBean>() {

        @Override
        public void onSuccess(LoginBean bean) {
            dismissDialog();
            preferences.edit().putString("username", edtAccount.getText().toString().trim()).apply();
            preferences.edit().putString("password", edtPassword.getText().toString().trim()).apply();
            Intent intent = new Intent(getContext(), EntryActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            dismissDialog();
            ToastUtil.showToast(getContext(), message);
        }

        @Override
        public void networkError(Throwable e) {
            dismissDialog();
            ToastUtil.showToast(getContext(), getContext().getResources().getString(R.string.network_error));
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (edtAccount.getText().toString().trim().length() > 0
                    && edtPassword.getText().toString().trim().length() > 0) {
                tvLogin.setBackgroundResource(R.drawable.shape_login);
                tvLogin.setClickable(true);
            } else {
                tvLogin.setBackgroundResource(R.drawable.shape_login_gray);
                tvLogin.setClickable(false);

            }
        }
    };

    @Override
    protected int getTranslucentColor() {
        return R.color.systembar_login;
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_login);
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void initPresenter() {
        unsubscribe();

        edtAccount.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);

        if (preferences.getString("username", "").length() > 0) {
            edtAccount.setText(preferences.getString("username", ""));
        }
        if (preferences.getString("password", "").length() > 0) {
            edtPassword.setText(preferences.getString("password", ""));
        }
        if (edtAccount.getText().toString().trim().length() > 0
                && edtPassword.getText().toString().trim().length() > 0) {
            tvLogin.setBackgroundResource(R.drawable.shape_login);
            tvLogin.setClickable(true);
            login();
        } else {
            tvLogin.setBackgroundResource(R.drawable.shape_login_gray);
            tvLogin.setClickable(false);
        }
    }

    private void login() {
        showProgressDialog();
        String str_random = RandomNum.getrandom();
        Map<String, String> map = new HashMap<>();
        map.put("method", "login");
        map.put("platform", "1");
        map.put("username", edtAccount.getText().toString().trim());
        map.put("password", edtPassword.getText().toString().trim());
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);

        mSubscription = Network.getVersionApi().getVersion(Network.WORK_URL, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @OnClick({R.id.tv_forget_pwd, R.id.tv_login, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_setting:
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
        }
    }
}
