package com.hyphenate.easeui.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    private static TextView currentAppNickName;

    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
//重写
    public static User getAppUserInfo(String username){
        if(userProvider != null)
            return userProvider.getAppUser(username);

        return null;
    }

    public static User getCurrentAppUserInfo(){
        String username = EMClient.getInstance().getCurrentUser();
        if(userProvider != null)
            return userProvider.getAppUser(username);

        return null;
    }


    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    /**
     * set user avatar
     * @param username
     */
    public static void setAppUserAvatar(Context context, String username, ImageView imageView){
        User user = getAppUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setAppUserNick(String username,TextView textView){
        if(textView != null){
            User user = getAppUserInfo(username);
            if(user != null && user.getMUserNick() != null){
                textView.setText(user.getMUserNick());
            }else{
                textView.setText(username);
            }
        }
    }

    public static void setCurrentAppAvater(FragmentActivity activity, ImageView ivProfileDefaultAvatar) {
            String username= EMClient.getInstance().getCurrentUser();
            setAppUserAvatar(activity,username,ivProfileDefaultAvatar);
    }

    public static void setCurrentAppNickName(TextView currentAppNickName) {
        String username = EMClient.getInstance().getCurrentUser();
        setAppUserNick(username,currentAppNickName);
    }

    public static void setCurrentAppUserNameWith(TextView currentAppUserName) {
        String username = EMClient.getInstance().getCurrentUser();
        setAppUserName("微信号：",username,currentAppUserName);
    }
    public static void setCurrentAppUserName(TextView currentAppUserName) {
        String username = EMClient.getInstance().getCurrentUser();
        setAppUserName("",username,currentAppUserName);
    }

    public static void setAppUserName(String suffix, String userName, TextView currentAppUserName) {
        currentAppUserName.setText(suffix+userName);
    }
}
