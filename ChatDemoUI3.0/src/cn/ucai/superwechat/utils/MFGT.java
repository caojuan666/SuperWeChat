package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.live.data.model.LiveRoom;
import cn.ucai.superwechat.live.ui.activity.LiveDetailsActivity;
import cn.ucai.superwechat.live.ui.activity.StartLiveActivity;
import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.ChatActivity;
import cn.ucai.superwechat.ui.GroupsActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.NewFriendsMsgActivity;
import cn.ucai.superwechat.ui.NewGroupActivity;
import cn.ucai.superwechat.ui.ProfileFriendActivity;
import cn.ucai.superwechat.ui.PublicGroupsActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SendAddContactActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(context, intent);
    }


    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    public static void startActivityForResult(Activity context, Intent intent, int requestCode) {

        context.startActivityForResult(intent, requestCode);
//        startActivity(context,RegsiterActivity.class);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoLogin(Activity context) {
        startActivity(context,LoginActivity.class);
  }
    public static void gotoRegister(Activity context) {
        startActivity(context,RegisterActivity.class);

    }
    public static void gotoSetting(Activity context) {
        startActivity(context, SettingsActivity.class);
    }
    public static void gotoUserProfile(Activity context) {
        startActivity(context, UserProfileActivity.class);
    }
    public static void gotoAddFirent(Activity context) {
        startActivity(context, AddContactActivity.class);
    }
    public static void gotoProfileFriend(Activity context, String username) {
        Intent intent = new Intent();
        intent.setClass(context, ProfileFriendActivity.class);
        intent.putExtra(I.User.USER_NAME,username);
        startActivity(context,intent);
    }
    public static void gotoAddFirentMsg(Activity context,String usename) {
        Intent intent = new Intent();
        intent.setClass(context, SendAddContactActivity.class);
        intent.putExtra(I.User.PASSWORD,usename);
        startActivity(context,intent);
    }
    public static void gotoAddNewFriends(Activity context) {
        startActivity(context, NewFriendsMsgActivity.class);
    }
    public static void gotoAppChat(Activity context,String usename) {
        Intent intent = new Intent();
        intent.setClass(context, ChatActivity.class);
        intent.putExtra("userId",usename);
        startActivity(context,intent);
    }
    public static void gotoGroup(Activity context){
        startActivity(context, GroupsActivity.class);

    }
    public static void gotoNewGroup(Activity context){
        startActivity(context, NewGroupActivity.class);

    }
    public static void gotoPublicGroup(Activity context){
        startActivity(context, PublicGroupsActivity.class);

    }
//    直播
    public static void gotoLiveActivity(Context context, LiveRoom liveRoom){
        Intent intent = new Intent();
        intent.setClass(context, StartLiveActivity.class);
        intent.putExtra("liveRoom",liveRoom);
        startActivity(context,intent);


    }
//    直播
    public static void gotoLiveDetails(Context context,LiveRoom liveRoom){
        Intent intent = new Intent();
        intent.setClass(context, LiveDetailsActivity.class);
        intent.putExtra("liveRoom",liveRoom);
        startActivity(context,intent);



    }



}