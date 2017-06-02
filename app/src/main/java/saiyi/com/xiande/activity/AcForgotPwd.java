package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.StringUtil;

public class AcForgotPwd extends BaseActivity implements View.OnClickListener {
    private Button mBtnOK;
    private EditText mEtUser;
    private EditText mEtPhone;
    private EditText mEtPwd;
    private EditText mEtVerify;
    private TextView mTvVerify;
    private ImageView mIvOpenPwd;
    private boolean mIsOpenPwd = false;
    private TimeCount time;

    int FontBlack;
    int FontGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);

        ((TextView)findViewById(R.id.tvTitle)).setText("忘记密码");
        findViewById(R.id.loLeft).setOnClickListener(this);

        FontBlack = getResources().getColor(R.color.FontBlack);
        FontGray = getResources().getColor(R.color.FontGray);

        mBtnOK = (Button)findViewById(R.id.btnOK);
        mBtnOK.setEnabled(false);

        mEtUser = (EditText)findViewById(R.id.etUserR);
        mEtPhone = (EditText)findViewById(R.id.etPhone);
        mEtPwd = (EditText)findViewById(R.id.etPwd);
        mEtVerify = (EditText)findViewById(R.id.etVerify);
        mTvVerify = (TextView)findViewById(R.id.tvVerify);
        setVerifyBtn(false);
        mIvOpenPwd = (ImageView)findViewById(R.id.ivOpen);

        mEtPhone.addTextChangedListener(mTextWatcher);
        mEtPwd.addTextChangedListener(mTextWatcher);
        mEtVerify.addTextChangedListener(mTextWatcher);

        mIvOpenPwd.setOnClickListener(this);
        mBtnOK.setOnClickListener(this);
        mTvVerify.setOnClickListener(this);

        time = new TimeCount(300000, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.ivOpen:
                if(mIsOpenPwd){//原来打开的，现在要关闭
                    mIsOpenPwd = false;
                    mIvOpenPwd.setImageResource(R.drawable.ic_close);
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{//原来关闭的，现在要打开
                    mIsOpenPwd = true;
                    mIvOpenPwd.setImageResource(R.drawable.ic_open);
                    mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.tvVerify:
                mTvVerify.setEnabled(false);
                Http.getAuthCode(mEtPhone.getText().toString(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mTvVerify.setEnabled(true);
                        switch (msg.what) {
                            case 1://成功
                                time.start();
                                Toast.makeText(AcForgotPwd.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 0://失败
                                Toast.makeText(AcForgotPwd.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
            case R.id.btnOK:
                mBtnOK.setEnabled(false);
                Http.editPwd(mEtUser.getText().toString(), mEtPhone.getText().toString(), mEtPwd.getText().toString(), mEtVerify.getText().toString(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mBtnOK.setEnabled(true);
                        switch (msg.what) {
                            case 1://成功
                                Toast.makeText(AcForgotPwd.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case 0://失败
                                Toast.makeText(AcForgotPwd.this, "修改失败", Toast.LENGTH_SHORT).show();
                                break;
                            case 3://验证码过期
                                Toast.makeText(AcForgotPwd.this, "修改失败-验证码过期", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
        }
    }

    private void setVerifyBtn(boolean b){
        mTvVerify.setEnabled(b);
        if(b){
            mTvVerify.setTextColor(FontBlack);
        }else {
            mTvVerify.setTextColor(FontGray);
        }
    }
    private void checkInputError(){
        mBtnOK.setEnabled(false);
        setVerifyBtn(false);
        if(!MyMethod.isUserNameFormatOK(String.valueOf(mEtUser.getText()))){
            return;
        }
        if(!StringUtil.isPhone(String.valueOf(mEtPhone.getText()))){
            return;
        }
        setVerifyBtn(true);
        if(!MyMethod.isPwdFormatOK(String.valueOf(mEtPwd.getText()))){
            return;
        }
        if(StringUtil.isEmpty(String.valueOf(mEtVerify.getText()))){
            return;
        }
        mBtnOK.setEnabled(true);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkInputError();//推迟执行，否则 EditView 的 Error 信息还没清除
                }
            },100);

        }
    };

    //计时器
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mTvVerify.setText("获取验证码");
            mTvVerify.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mTvVerify.setClickable(false);//防止重复点击
            mTvVerify.setText(millisUntilFinished / 1000 + "s" + "重发");
        }
    }
}
