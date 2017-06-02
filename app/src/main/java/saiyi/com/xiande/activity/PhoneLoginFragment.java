package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DOrderInfo;
import saiyi.com.xiande.data.DStudent;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.PreferencesUtils;
import saiyi.com.xiande.utils.StringUtil;

/**
 * 描述：使用电话登录
 * 创建作者：黎丝军
 * 创建时间：2017/1/12 15:55
 */

public class PhoneLoginFragment extends Fragment implements View.OnClickListener {

    private Button mBtnLogin;
    private CheckBox mCbAgree;
    private EditText mEtPhone;
    private EditText mEtVerify;
    private TextView mTvVerify;
    private TimeCount time;
    int FontBlack;
    int FontGray;

    private MyData mMyData;
    private PreferencesUtils mPreferencesUtils;
    private SVProgressHUD svProgressHUD;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_register, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyData = BaseApplication.getInstance().getMyData();
        mPreferencesUtils = new PreferencesUtils(getActivity().getApplicationContext(), AppConstants.Preferences_User);

        FontBlack = getResources().getColor(R.color.FontBlack);
        FontGray = getResources().getColor(R.color.FontGray);

        mBtnLogin = (Button)getActivity().findViewById(R.id.btn_login);
        mBtnLogin.setEnabled(false);

        mEtPhone = (EditText)getActivity().findViewById(R.id.etPhoneR);
        mEtVerify = (EditText)getActivity().findViewById(R.id.etVerifyR);
        mTvVerify = (TextView)getActivity().findViewById(R.id.tvVerifyR);
        setVerifyBtn(false);
        mCbAgree = (CheckBox)getActivity().findViewById(R.id.cbAgreeR);

        mEtPhone.addTextChangedListener(mTextWatcher);
        mEtVerify.addTextChangedListener(mTextWatcher);

        getActivity().findViewById(R.id.tvAgreeR).setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mCbAgree.setOnClickListener(this);
        mTvVerify.setOnClickListener(this);

        time = new TimeCount(90000, 1000);
        svProgressHUD = new SVProgressHUD(getContext());


        svProgressHUD = new SVProgressHUD(getActivity());
        mMyData.mLoginType = 1;
        mPreferencesUtils.clear();


    }

    private void setVerifyBtn(boolean b){
        mTvVerify.setEnabled(b);
        if(b){
            mTvVerify.setTextColor(FontBlack);
        }else {
            mTvVerify.setTextColor(FontGray);
        }
    }

    private void toActivity(Class<?> pClass){
        getActivity().startActivity(new Intent(getActivity(),pClass));
        svProgressHUD.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cbAgreeR:
                checkInputError();
                break;
            case R.id.tvVerifyR:
                mTvVerify.setEnabled(false);
                Http.getAuthCode(mEtPhone.getText().toString(),new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        mTvVerify.setEnabled(true);
                        switch (msg.what){
                            case 1://成功
                                time.start();
                                Toast.makeText(getActivity(),"获取验证码成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 0://失败
                                Toast.makeText(getActivity(),"获取验证码失败",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
            case R.id.tvAgreeR://服务协议
                startActivity(new Intent(getActivity(), AcAgreement.class));
                break;
            case R.id.btn_login:
                svProgressHUD.show();
                Login();

               // toActivity(HomeActivity.class);
                break;
        }
    }

    private void Login(){
        mBtnLogin.setEnabled(false);
        Http.login(mEtPhone.getText().toString().trim(),mEtVerify.getText().toString().trim(),mLoginHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkInputError();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private Handler mLoadUserInfoHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what != 1){
                mBtnLogin.setEnabled(true);
            }
            switch (msg.what) {
                case 1://成功
                    if(msg.obj != null) {
                        //Toast.makeText(getActivity(), "获取个人信息成功", Toast.LENGTH_SHORT).show();
                        mMyData.mStudent = (DStudent.fromJSONArray((JSONArray) msg.obj)).get(0);
                        //mMyData.mStudent.mIsValid = 3;//个人信息验证成功
                        switch (mMyData.mStudent.mIsValid) {
                            default:
                            case 0://初始状态
                                svProgressHUD.dismiss();
                                final Intent intent = new Intent(getContext(),AcUserInfo.class);
                                intent.putExtra("phone",mEtPhone.getText().toString().trim());
                                startActivity(intent);
                                break;
                            case 1://已提交
                            case 2://审核失败
                                svProgressHUD.dismiss();
                                toActivity(AcUserInfoSubmit.class);
                                break;
                            case 3://审核通过
                                Http.getMyOrder_NoOver(mMyData.mStudent.mPhone, mLoadOrderHandler);
                                break;
                        }
                    } else {
                        svProgressHUD.dismiss();
                        final Intent intent = new Intent(getContext(),AcUserInfo.class);
                        intent.putExtra("phone",mEtPhone.getText().toString().trim());
                        startActivity(intent);
                    }
                    break;
                case 0://失败
                    svProgressHUD.dismiss();
                    Toast.makeText(getActivity(), "获取个人信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private Handler mLoadOrderHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what != 1){
                mBtnLogin.setEnabled(true);
                svProgressHUD.dismiss();
            }
            if (msg.what == 1) {
                LoginUtil.setLogin(mEtPhone.getText().toString().trim(),1);
                if (msg.obj != null) {
                    mMyData.mCurOrder = DOrderInfo.fromJSONArray((JSONArray)msg.obj).get(0);
                    mMyData.mCurOrderId = MyMethod.parseInt(mMyData.mCurOrder.mOrderId);
                    mMyData.mUserId = LoginUtil.getPhone();
                    Http.getBike(mMyData.mUserId,mMyData.mCurOrder.mBikeEncode,mLoadBikeHandler);
                }else{
                    //startActivity(new Intent(getActivity(), AcNearBike.class));
                    toActivity(HomeActivity.class);
                }
            }else {
                //startActivity(new Intent(getActivity(), AcNearBike.class));
                toActivity(HomeActivity.class);
            }
        }
    };

    private Handler mLoadBikeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mBtnLogin.setEnabled(true);
            svProgressHUD.dismiss();
            if (msg.what == 1) {
                if (msg.obj != null) {
                    mMyData.mScanBike = DBike.fromJSONArray((JSONArray) msg.obj).get(0);
                    toActivity(AcBike.class);
                }
            }
        }
    };

    private void checkInputError(){
        mBtnLogin.setEnabled(false);
        setVerifyBtn(false);
        if(!StringUtil.isPhone(String.valueOf(mEtPhone.getText()))){
            return;
        }
        setVerifyBtn(true);
        if(StringUtil.isEmpty(String.valueOf(mEtVerify.getText()))){
            return;
        }
        if(!mCbAgree.isChecked()){
            return;
        }
        mBtnLogin.setEnabled(true);
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

    //学生登录登录
    private Handler mLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://成功
                    Http.getUserInfo(mEtPhone.getText().toString().trim(),mLoadUserInfoHandler);
                    break;
                default:
                    Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                    mBtnLogin.setEnabled(true);
                    svProgressHUD.dismiss();
                    break;
            }

        }
    };

}
