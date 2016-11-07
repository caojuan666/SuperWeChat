package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class ProfileFriendActivity extends BaseActivity {
    @BindView(R.id.iv_profile_avater)
    ImageView ivProfileAvater;
    @BindView(R.id.tv_profile_username)
    TextView tvProfileUsername;
    @BindView(R.id.tv_profile_nickname)
    TextView tvProfileNickname;
    @BindView(R.id.ctitle_ivback)
    ImageView ctitleIvback;
    @BindView(R.id.ctitle_tvTitle)
    TextView ctitleTvTitle;
    User user =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
         user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if(user==null){
            MFGT.finish(this);
        }
        initView();

    }

    private void initView() {
        ctitleIvback.setVisibility(View.VISIBLE);
        ctitleTvTitle.setVisibility(View.VISIBLE);
        tvProfileUsername.setVisibility(View.VISIBLE);
        tvProfileNickname.setVisibility(View.VISIBLE);
        ctitleTvTitle.setText(getString(R.string.userinfo_txt_profile));

        setUserInfo();
    }
    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this,user.getMUserName(),ivProfileAvater);
        EaseUserUtils.setAppUserNick(user.getMUserName(),tvProfileNickname);
        EaseUserUtils.setAppUserNameWith(user.getMUserName(),tvProfileUsername);

    }

    @OnClick(R.id.ctitle_ivback)
    public void onClick() {

    }
}
