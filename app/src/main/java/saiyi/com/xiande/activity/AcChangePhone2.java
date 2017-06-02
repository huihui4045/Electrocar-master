package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.StringUtil;

/**
 * 修改手机号第一步
 */
public class AcChangePhone2 extends BaseActivity implements View.OnClickListener{
    private TextView mTvRight;
    private EditText mEtPhone;
    private MyData mMyData;
    private EditText mEtVerify;
    private TextView mTvVerify;
    private SVProgressHUD svProgressHUD;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changephone2);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView) findViewById(R.id.tvTitle)).setText("更换联系方式2/2");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mTvRight.setText("完成");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(this);
        mEtVerify = (EditText)findViewById(R.id.etVerifyR);
        mTvVerify = (TextView)findViewById(R.id.tvVerifyR);
        mTvVerify.setOnClickListener(this);
        svProgressHUD = new SVProgressHUD(this);

        mEtPhone = (EditText)findViewById(R.id.etPhoneR);
        mEtPhone.addTextChangedListener(mTextWatcher);
        mEtVerify.addTextChangedListener(mTextWatcher);
        time = new TimeCount(90000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (time != null) {
            time.cancel();
            time = null;
        }
    }

    //返回true表示没错了
    private boolean checkInputError(boolean isShowError){
        setVerifyBtn(false);
        if(!StringUtil.isPhone(String.valueOf(mEtPhone.getText()))){
            if(isShowError) {
                mEtPhone.setError("手机号格式不对");
            }
            return false;
        }
        setVerifyBtn(true);
        if(StringUtil.isEmpty(String.valueOf(mEtVerify.getText()))){
            if(isShowError) {
                mEtVerify.setError("验证码不能为空");
            }
            return false;
        }
        return true;
    }
    private void setVerifyBtn(boolean b){
        mTvVerify.setEnabled(b);
        if(b){
            mTvVerify.setTextColor(getResources().getColor(R.color.FontBlack));
        }else {
            mTvVerify.setTextColor(getResources().getColor(R.color.FontGray));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft:
                finish();
                break;
            case R.id.tvVerifyR:
                mTvVerify.setEnabled(false);
                Http.getAuthCode(mEtPhone.getText().toString(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mTvVerify.setEnabled(true);
                        switch (msg.what) {
                            case 1://成功
                                time.start();
                                Toast.makeText(AcChangePhone2.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 0://失败
                                Toast.makeText(AcChangePhone2.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
            case R.id.tvRight:
                if(checkInputError(true)) {
                    svProgressHUD.show();
                    Http.updatePhone(mMyData.mUserId, mEtPhone.getText().toString(), mEtVerify.getText().toString(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            svProgressHUD.dismiss();
                            switch (msg.what) {
                                case 1://成功
                                    mMyData.mStudent.mPhone = mEtPhone.getText().toString();
                                    setResult(1);//修改成功
                                    finish();
                                case 0:
                                    Toast.makeText(AcChangePhone2.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(AcChangePhone2.this, "手机号已被使用", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(AcChangePhone2.this, "验证码过期", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
                break;
        }
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
                    checkInputError(false);//推迟执行，否则 EditView 的 Error 信息还没清除
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
