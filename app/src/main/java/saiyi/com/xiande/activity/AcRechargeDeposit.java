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
import saiyi.com.xiande.data.DDict;
import saiyi.com.xiande.data.DWXpayInfo;
import saiyi.com.xiande.data.MyData;

/**
 * 充押金
 */
public class AcRechargeDeposit extends BaseActivity implements View.OnClickListener {
    private ImageView mIvWeixinCheck;
    private ImageView mIvZhifuCheck;
    private Button mBtnOk;
    private int mPayType;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;

    private TextView mTvMoney;

    private IWXAPI mWXApi;//微信支付的API
    private boolean mIsWXRegistApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_deposit);
        mMyData = BaseApplication.getInstance().getMyData();

        mWXApi = WXAPIFactory.createWXAPI(this, AppConstants.WXApp_id);
        mIsWXRegistApp = mWXApi.registerApp(AppConstants.WXApp_id);

        ((TextView)findViewById(R.id.tvTitle)).setText("充押金");
        findViewById(R.id.loLeft).setOnClickListener(this);

        mIvWeixinCheck = (ImageView)findViewById(R.id.ivWeixinCheck);
        mIvZhifuCheck = (ImageView)findViewById(R.id.ivZhifuCheck);
        setPayType(AppConstants.Weixin);
        svProgressHUD = new SVProgressHUD(this);

        mTvMoney = (TextView)findViewById(R.id.tvMoney);
        mTvMoney.setOnClickListener(this);
        findViewById(R.id.loWeixin).setOnClickListener(this);
        findViewById(R.id.loZhifu).setOnClickListener(this);
        mBtnOk = (Button)findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        setChoose(mTvMoney,true);

        if(mMyData.mDict == null){
            svProgressHUD.show();
            Http.getDict(new Handler() {//获取系统定义的一些商家标准数据
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        if(msg.obj != null) {
                            svProgressHUD.dismiss();
                            mMyData.mDict = DDict.fromJSONArray((JSONArray) msg.obj).get(0);
                            mTvMoney.setText((int)(mMyData.mDict.mDeposit) + "元");
                        }
                    }
                }
            });
        }else{
            mTvMoney.setText((int)(mMyData.mDict.mDeposit) + "元");
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.tvMoney:
                setChoose(mTvMoney,true);
                mBtnOk.setEnabled(true);
                break;
            case R.id.loWeixin:
                setPayType(AppConstants.Weixin);
                break;
            case R.id.loZhifu:
                setPayType(AppConstants.Zhifu);
                break;
            case R.id.btnOK:
                mBtnOk.setEnabled(false);
                if(mPayType == AppConstants.Zhifu) {
                    Http.getAlipayInfo(mMyData.mUserId, mMyData.mDict.mDeposit, mGetAlipayInfoHandler);
                }else if(mPayType == AppConstants.Weixin) {
                    Http.getWXpayInfo(mMyData.mUserId, mMyData.mDict.mDeposit, mGetWXpayInfoHandler);
                }
                //Http.getAlipayInfo(mMyData.mUserId, 0.01,mGetAlipayInfoHandler);
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
                Toast.makeText(AcRechargeDeposit.this, "充值失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AcRechargeDeposit.this, "充值失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AcRechargeDeposit.this, "充值失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void alipay(final String orderInfo){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(AcRechargeDeposit.this);
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
                        Toast.makeText(AcRechargeDeposit.this, "支付成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(AcRechargeDeposit.this, AcMoney.class));
                            }
                        }, 1000);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(AcRechargeDeposit.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        //showNormalDialog();
    }

    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(AcRechargeDeposit.this);
        normalDialog.setMessage("亲，充值金额只能用于骑行，不能退款哦！");
        normalDialog.setPositiveButton("知道了",null);
        normalDialog.show();
    }
}
