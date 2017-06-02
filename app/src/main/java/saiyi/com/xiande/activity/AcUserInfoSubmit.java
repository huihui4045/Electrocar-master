package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.data.MyData;

/**
 * 个人信息提交后的界面，包含：已提交和审核失败
 */
public class AcUserInfoSubmit extends BaseActivity implements View.OnClickListener {
    private ImageView mIv;
    private TextView mTvBig;
    private TextView mTvSmall;
    private Button  mBtn;
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_submit);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("个人信息认证");
        findViewById(R.id.loLeft).setVisibility(View.GONE);
        findViewById(R.id.ivLeft).setVisibility(View.GONE);
        findViewById(R.id.btnOK).setOnClickListener(this);

        mIv = (ImageView)findViewById(R.id.iv);
        mTvBig = (TextView)findViewById(R.id.tvSuc);
        mTvSmall = (TextView)findViewById(R.id.tvWate);
        mBtn = (Button)findViewById(R.id.btnOK);
        mBtn.setOnClickListener(this);

        if(mMyData.mStudent.mIsValid == 2){//审核失败
            mIv.setImageResource(R.drawable.warn);
            mTvBig.setText("审核失败");
            mTvSmall.setText("请重新提交身份认证信息");
            mBtn.setText("重新填写");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                if(mMyData.mStudent.mIsValid == 2) {//审核失败
                    startActivity(new Intent(this,AcUserInfo.class));
                    finish();
                }else {
                    setResult(RESULT_OK, getIntent()); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            finish();
            startActivity(new Intent(this, Login.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
