package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btt_register_login, R.id.btt_register_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btt_register_login:
                MFGT.gotoLogin(this);
                break;
            case R.id.btt_register_signup:
                MFGT.gotoRegister(this);
                break;
        }
    }
}
