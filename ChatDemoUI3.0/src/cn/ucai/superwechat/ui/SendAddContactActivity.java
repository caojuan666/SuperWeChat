package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;



public class SendAddContactActivity extends BaseActivity {
    private static final String TAG = SendAddContactActivity.class.getSimpleName();
    @BindView(R.id.ctitle_ivback)
    ImageView ctitleIvback;
    @BindView(R.id.ctitle_tvTitle)
    TextView ctitleTvTitle;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.send)
    Button send;
    private ProgressDialog progressDialog;
    String usename;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        usename=getIntent().getStringExtra(I.User.PASSWORD);
        if(usename==null){
            MFGT.finish(this);
        }
        initView();

    }

    private void initView() {
        ctitleIvback.setVisibility(View.VISIBLE);
        ctitleTvTitle.setVisibility(View.VISIBLE);
        ctitleTvTitle.setText(getString(R.string.add_friend));
//        send.setVisibility(View.VISIBLE);
        L.e(TAG, "current user:" + EaseUserUtils.getCurrentAppUserInfo());

        msg = getString(R.string.addcontact_send_msg_prefix)
                + EaseUserUtils.getCurrentAppUserInfo().getMUserNick();
        etMsg.setText(msg);
    }
    @OnClick({R.id.ctitle_ivback, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctitle_ivback:
                MFGT.finish(this);
                break;
            case R.id.send:
                sendMess();
                break;
        }
    }

    private void sendMess() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.addcontact_adding);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(usename, msg);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                            MFGT.finish(SendAddContactActivity.this);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                            MFGT.finish(SendAddContactActivity.this);
                        }
                    });
                }
            }
        }).start();
    }
}
