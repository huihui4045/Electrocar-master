package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DBikeType;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.StringUtil;

/**
 * 商户登录，录入车辆编号
 */
public class AcSCode extends BaseActivity implements View.OnClickListener{
    private Button mBtnOk;
    private EditText mEtCord;
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_code);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView) findViewById(R.id.tvTitle)).setText("输入设备编号");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mBtnOk = (Button)findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mEtCord = (EditText)findViewById(R.id.etCode);
        mEtCord.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft:
                finish();
                break;
            case R.id.btnOK:
                startActivityForResult(new Intent(this, saiyi.com.xiande.zbar.CaptureActivity.class).putExtra("type",2), 1);
                break;
        }
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {//成功返回
            switch (requestCode) {
                case 1://扫码返回
                    final String result = data.getStringExtra("result");
                    if(MyMethod.isBikeEanCode(result)) {
                        Http.addBike(result,mEtCord.getText().toString(),mMyData.msChooseBikeType.mTypeId,mMyData.mUserId,mMyData.msChooseStop.mId,new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 1:
                                        Toast.makeText(AcSCode.this, "车辆录入成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(AcSCode.this, "查询车辆类型失败", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this,"二维码格式错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void checkInputError(){
        mBtnOk.setEnabled(false);
        if(StringUtil.isEmpty(String.valueOf(mEtCord.getText()))){
            return;
        }
        mBtnOk.setEnabled(true);
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
}
