package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * 更换联系方式
 */
public class AcChangeConnect extends BaseActivity implements View.OnClickListener{
    private TextView mTvRight;
    private EditText mEtPhone;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;
    private boolean mIsEditStatus = false;//是否处于编辑状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeconnect);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView) findViewById(R.id.tvTitle)).setText("更换联系方式");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mTvRight.setText("编辑");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(this);
        svProgressHUD = new SVProgressHUD(this);

        mEtPhone = (EditText)findViewById(R.id.etPhone);
        mEtPhone.setText(mMyData.mStudent.mPhone);
        mEtPhone.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft:
                finish();
                break;
            case R.id.tvRight:
                if(!mIsEditStatus){
                    mEtPhone.setEnabled(true);
                    mTvRight.setText("完成");
                    mIsEditStatus = true;
                }else{
                    svProgressHUD.show();
                    Http.updatePhone(mMyData.mUserId,mEtPhone.getText().toString(),new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            svProgressHUD.dismiss();
                            if(msg.what == 1){
                                mTvRight.setText("编辑");
                                mEtPhone.setEnabled(false);
                                mMyData.mStudent.mPhone = mEtPhone.getText().toString();
                                Toast.makeText(AcChangeConnect.this, "修改成功", Toast.LENGTH_SHORT).show();
                                mIsEditStatus = false;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                            }else{
                                Toast.makeText(AcChangeConnect.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
