package saiyi.com.xiande.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.svprogresshud.SVProgressHUD;
import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.PreferencesUtils;

public class SellerLoginFragment extends Fragment implements View.OnClickListener{
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPwd;
    private TextView mTvForgotPwd;
    private ImageView mIvOpenPwd;
    private boolean mIsOpenPwd = false;
    private MyData mMyData;
    private PreferencesUtils mPreferencesUtils;
    private SVProgressHUD svProgressHUD;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyData = BaseApplication.getInstance().getMyData();
        mPreferencesUtils = new PreferencesUtils(getActivity().getApplicationContext(), AppConstants.Preferences_User);

        mBtnLogin = (Button)getActivity().findViewById(R.id.btnLogin);
        mBtnLogin.setEnabled(false);

        mEtUser = (EditText)getActivity().findViewById(R.id.etUser);
        mEtPwd = (EditText)getActivity().findViewById(R.id.etPwd);
        mIvOpenPwd = (ImageView)getActivity().findViewById(R.id.ivOpen);
        mTvForgotPwd = (TextView)getActivity().findViewById(R.id.tvForgotPwd);

        mEtUser.addTextChangedListener(mTextWatcher);
        mEtPwd.addTextChangedListener(mTextWatcher);

        mIvOpenPwd.setOnClickListener(this);
        getActivity().findViewById(R.id.tvForgotPwd).setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        svProgressHUD = new SVProgressHUD(getActivity());

        mMyData.mLoginType = 2;
        setForgotPwdEnabled(true);
        mPreferencesUtils.clear();
    }

    private void setForgotPwdEnabled(boolean b){
        if(b){
            mTvForgotPwd.setEnabled(true);
            mTvForgotPwd.setTextColor(getResources().getColor(R.color.FontBlack));
        }else{
            mTvForgotPwd.setEnabled(false);
            mTvForgotPwd.setTextColor(getResources().getColor(R.color.FontGray));
        }
    }

    private void toActivity(Class<?> pClass){
        getActivity().startActivity(new Intent(getActivity(),pClass));
        svProgressHUD.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.tvForgotPwd:
                //startActivity(new Intent(getActivity(),AcForgotPwd.class));
                toActivity(AcForgotPwd.class);
                break;
            case R.id.btnLogin:
                svProgressHUD.show();
                Login();
                break;
        }
    }

    private void Login() {
        mBtnLogin.setEnabled(false);
        Http.sellerLogin(mEtUser.getText().toString(), mEtPwd.getText().toString(), mSellerLoginHandler);
    }

    //商家用户登录
    private Handler mSellerLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            svProgressHUD.dismiss();
            mBtnLogin.setEnabled(true);
            switch (msg.what) {
                case 1://成功
                    mMyData.mUserId = mEtUser.getText().toString();
                    mMyData.mPwd = mEtPwd.getText().toString();
                    mMyData.mIsSeller = true;
                    LoginUtil.setLogin(mEtUser.getText().toString().trim(),2);
                    toActivity(AcSBikeTypeList.class);
                    break;
                case 0://失败
                    Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 2://失败
                    Toast.makeText(getActivity(), "账户不存在", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        checkInputError();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void checkInputError(){
        mBtnLogin.setEnabled(false);
        if(!MyMethod.isPwdFormatOK(String.valueOf(mEtPwd.getText()))){
            return;
        }
        if(!MyMethod.isUserNameFormatOK(String.valueOf(mEtUser.getText()))){
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

}
