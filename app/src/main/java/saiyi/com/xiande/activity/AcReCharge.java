package saiyi.com.xiande.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import saiyi.com.xiande.Alipay.PayResult;
import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DStopPosition;
import saiyi.com.xiande.data.DWXpayInfo;
import saiyi.com.xiande.data.MyData;

public class AcReCharge extends BaseActivity implements View.OnClickListener {
    private EditText mEtMoney;
    private ImageView mIvWeixinCheck;
    private ImageView mIvZhifuCheck;
    private Button mBtnOk;
    private int mPayType;
    private float mMoney;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;

    private TextView mTvMoney20;
    private TextView mTvMoney50;
    private TextView mTvMoney100;
    private TextView mTvMoney200;

    private IWXAPI mWXApi;//微信支付的API
    private boolean mIsWXRegistApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        mMyData = BaseApplication.getInstance().getMyData();

        mWXApi = WXAPIFactory.createWXAPI(this, AppConstants.WXApp_id);
        mIsWXRegistApp = mWXApi.registerApp(AppConstants.WXApp_id);

        ((TextView)findViewById(R.id.tvTitle)).setText("充值");
        findViewById(R.id.loLeft).setOnClickListener(this);

        mEtMoney = (EditText)findViewById(R.id.etMoney);
        mIvWeixinCheck = (ImageView)findViewById(R.id.ivWeixinCheck);
        mIvZhifuCheck = (ImageView)findViewById(R.id.ivZhifuCheck);
        setPayType(AppConstants.Weixin);
        svProgressHUD = new SVProgressHUD(this);

        mTvMoney20 = (TextView)findViewById(R.id.tvMoney20);
        mTvMoney50 = (TextView)findViewById(R.id.tvMoney50);
        mTvMoney100 = (TextView)findViewById(R.id.tvMoney100);
        mTvMoney200 = (TextView)findViewById(R.id.tvMoney200);
        findViewById(R.id.tvMoney20).setOnClickListener(this);
        findViewById(R.id.tvMoney50).setOnClickListener(this);
        findViewById(R.id.tvMoney100).setOnClickListener(this);
        findViewById(R.id.tvMoney200).setOnClickListener(this);
        findViewById(R.id.loWeixin).setOnClickListener(this);
        findViewById(R.id.loZhifu).setOnClickListener(this);
        mBtnOk = (Button)findViewById(R.id.btnOK);
        mBtnOk.setEnabled(false);
        mBtnOk.setOnClickListener(this);
    }

    void setPayType(int type){
        if(type == AppConstants.Weixin){
            mPayType = AppConstants.Weixin;
            mIvWeixinCheck.setImageResource(R.drawable.check_on);
            mIvZhifuCheck.setImageResource(R.drawable.check_off);
        }else if(type == AppConstants.Zhifu){
            mPayType = AppConstants.Zhifu;
            mIvWeixinCheck.setImageResource(R.drawable.check_off);
            mIvZhifuCheck.setImageResource(R.drawable.check_on);
        }
    }

    //设置选中充值金额框
    private void setChoose(TextView textView,boolean isChoose){
        if(isChoose){
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundColor(getResources().getColor(R.color.FontBlue));
        }else{
            textView.setTextColor(getResources().getColor(R.color.FontBlue));
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_blue));
        }
    }
    private void setChooseTv(int viewId){
        setChoose(mTvMoney20,false);
        setChoose(mTvMoney50,false);
        setChoose(mTvMoney100,false);
        setChoose(mTvMoney200,false);
        switch (viewId) {
            case R.id.tvMoney20:
                setChoose(mTvMoney20, true);
                mMoney = 20;
                break;
            case R.id.tvMoney50:
                setChoose(mTvMoney50, true);
                mMoney = 50;
                break;
            case R.id.tvMoney100:
                setChoose(mTvMoney100, true);
                mMoney = 100;
                break;
            case R.id.tvMoney200:
                setChoose(mTvMoney200, true);
                mMoney = 200;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.tvMoney20:
            case R.id.tvMoney50:
            case R.id.tvMoney100:
            case R.id.tvMoney200:
                setChooseTv(v.getId());
                mBtnOk.setEnabled(true);
                break;
            case R.id.loWeixin:
                setPayType(AppConstants.Weixin);
                break;
            case R.id.loZhifu:
                setPayType(AppConstants.Zhifu);
                break;
            case R.id.btnOK:
                //double money = MyMethod.parseDouble(mEtMoney.getText().toString());
                if(mMoney >= 1) {
                    mBtnOk.setEnabled(false);
                    if(mPayType == AppConstants.Zhifu) {
                        Http.getAlipayInfo(mMyData.mUserId, mMoney, mGetAlipayInfoHandler);
                    }else if(mPayType == AppConstants.Weixin) {
                        Http.getWXpayInfo(mMyData.mUserId, mMoney, mGetWXpayInfoHandler);
                    }
                }else{
                    Toast.makeText(AcReCharge.this, "金额输入有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Handler mGetWXpayInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBtnOk.setEnabled(true);
            if (msg.what == 1 && msg.obj != null) {
                DWXpayInfo dwXpayInfo = DWXpayInfo.fromJSONArray((JSONArray) msg.obj).get(0);
                boolean b = mWXApi.sendReq(dwXpayInfo.toPayReq());
            } else {
                Toast.makeText(AcReCharge.this, "充值失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler mGetAlipayInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBtnOk.setEnabled(true);
            if (msg.what == 1 && msg.obj != null) {
                try {
                    JSONObject jsonObject = (JSONObject) ((JSONArray) msg.obj).get(0);
                    alipay(jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AcReCharge.this, "充值失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AcReCharge.this, "充值失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void alipay(final String orderInfo){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(AcReCharge.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(AcReCharge.this, "支付成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(AcReCharge.this, AcMoney.class));
                            }
                        }, 1000);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(AcReCharge.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onStart(){
        super.onStart();
        showNormalDialog();
    }

    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(AcReCharge.this);
        normalDialog.setMessage("亲，充值金额只能用于骑行，不能退款哦！");
        normalDialog.setPositiveButton("知道了",null);
        normalDialog.show();
    }
}
