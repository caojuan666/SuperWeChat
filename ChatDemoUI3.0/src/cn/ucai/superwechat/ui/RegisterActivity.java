/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.MD5;
import cn.ucai.superwechat.utils.MFGT;

import static cn.ucai.superwechat.R.id.etregister_confire_password;
import static cn.ucai.superwechat.R.id.etregister_password;
import static cn.ucai.superwechat.R.id.etregister_username;

/**
 * register screen
 */
public class RegisterActivity extends BaseActivity {
    @BindView(etregister_username)
    EditText etregisterUsername;
    @BindView(R.id.etregister_nickname)
    EditText etregisterNickname;
    @BindView(etregister_password)
    EditText etregisterPassword;
    @BindView(etregister_confire_password)
    EditText etregisterConfirePassword;

    String username;
    String pwd;
    String confirm_pwd;
    String nickname;
    ProgressDialog pd = null;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.em_activity_register);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    public void register() {
        username = etregisterUsername.getText().toString().trim();
        nickname = etregisterNickname.getText().toString().trim();
        pwd = etregisterPassword.getText().toString().trim();
        confirm_pwd = etregisterConfirePassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etregisterUsername.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            Toast.makeText(this, getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
            etregisterUsername.requestFocus();
            return;

        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etregisterPassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etregisterConfirePassword.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();
            registerAppService();
//            unregisterAppService();
        }

    }

    private void unregisterAppService() {
//        网络请求
        NetDao.unregister(mContext, username, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });


    }

    private void registerAppService() {
        NetDao.register(mContext, username, nickname, pwd, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result == null) {
                    pd.dismiss();
                } else {
//                    注册环信
                    if (result != null && result.isRetMsg()) {
                        registerEMService();

                    } else {
                        if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                            CommonUtils.showMsg(result.getRetCode());
                        } else {
                            unregisterAppService();
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();

            }
        });
//        网络请求

    }
    //环信注册
    private void registerEMService() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, MD5.getMessageDigest(pwd));
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // save current user
                            SuperWeChatHelper.getInstance().setCurrentUserName(username);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    unregisterAppService();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void onBackPressed() {
        MFGT.finish(this);
    }

    @OnClick(R.id.btt_register)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btt_register:
                register();
                break;

        }

    }
}
