package cn.ucai.superwechat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.security.mobile.module.commonutils.crypto.ReflectUtil;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.EasyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by Administrator on 2016/11/4 0004.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.iv_profile_default_avatar)
    ImageView ivProfileDefaultAvatar;
    @BindView(R.id.tv_profile_usernick)
    TextView tvProfileUsernick;
    @BindView(R.id.tv_profile_username)
    TextView tvProfileUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        setUserInfo();

    }

    private void setUserInfo() {
        EaseUserUtils.setCurrentAppAvater(getActivity(),ivProfileDefaultAvatar);
        EaseUserUtils.setCurrentAppNickName(tvProfileUsernick);
        EaseUserUtils.setCurrentAppUserNameWith(tvProfileUsername);

    }

    @OnClick({R.id.layout_profile_user, R.id.tv_profile_money, R.id.tv_profile_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_profile_user:
                MFGT.gotoUserProfile(getActivity());
                break;
            case R.id.tv_profile_money:
                RedPacketUtil.startChangeActivity(getActivity());
                break;
            case R.id.tv_profile_setting:
                MFGT.gotoSetting(getActivity());
                break;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
            outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }
}