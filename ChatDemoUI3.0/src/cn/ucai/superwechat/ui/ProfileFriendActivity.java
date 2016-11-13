package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

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
    String username = null;
    @BindView(R.id.btton_sendMessage)
    Button bttonSendMessage;
    @BindView(R.id.btton_play)
    Button bttonPlay;
    @BindView(R.id.btton_add_friend)
    Button bttonAddFriend;
    private boolean isFriend;
    User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra(I.User.USER_NAME);
        L.e("---------ProfileFriendActivity,onCreate-----------user" + username);
        if (username == null) {
            MFGT.finish(this);
            return;
        }
        initView();
        user = SuperWeChatHelper.getInstance().getAppContactList().get(username);
        if (user == null) {
            isFriend = false;
        } else {
            setUserInfo();
            isFriend = true;
        }
//        如果不是好友去服务器上去获取
        syUserInfo();
//
        isFriend(isFriend);
    }

    private void synFail() {
        MFGT.finish(this);
        return;

    }

    private void syUserInfo() {
        NetDao.syncUserInfo(this, username, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null && result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        if (u != null) {

                            if (isFriend) {

                                SuperWeChatHelper.getInstance().saveAppContact(user);

                            }
                            user = u;
                            setUserInfo();
                        } else {
                            synFail();
                        }
                    } else {
                        synFail();
                    }
                }else {
                    synFail();
                }
            }

            @Override
            public void onError(String error) {
                synFail();

            }
        });


    }

    private void initView() {

        ctitleIvback.setVisibility(View.VISIBLE);
        ctitleTvTitle.setVisibility(View.VISIBLE);
        tvProfileUsername.setVisibility(View.VISIBLE);
        tvProfileNickname.setVisibility(View.VISIBLE);
        ctitleTvTitle.setText(getString(R.string.userinfo_txt_profile));
//        setUserInfo();

    }

    private void isFriend(boolean isFriend) {
        if (isFriend) {
            bttonSendMessage.setVisibility(View.VISIBLE);
            bttonPlay.setVisibility(View.VISIBLE);
        } else {
            bttonAddFriend.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
        L.e("---------------------user" + user);
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), ivProfileAvater);
        EaseUserUtils.setAppUserNick(user.getMUserNick(), tvProfileNickname);
        EaseUserUtils.setAppUserNameWith(user.getMUserName(), tvProfileUsername);

    }


    @OnClick({R.id.ctitle_ivback, R.id.btton_sendMessage, R.id.btton_play, R.id.btton_add_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctitle_ivback:
                MFGT.finish(this);
                break;
            case R.id.btton_sendMessage:
                MFGT.gotoAppChat(this, user.getMUserName());
                break;
            case R.id.btton_play:
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                            .putExtra("isComingCall", false));
                    // videoCallBtn.setEnabled(false);

                }
                break;
            case R.id.btton_add_friend:
                MFGT.gotoAddFirentMsg(this, user.getMUserName());
                break;
        }
    }
}
