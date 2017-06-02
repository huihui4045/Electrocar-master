package saiyi.com.xiande.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.base.PopUpView;
import saiyi.com.xiande.data.DProblem;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.StringUtil;


public class AcFault extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;
    private EditText mEtInput;
    private EditText mEtPhone;
    private EditText mEtEan;
    private TextView mTvNum;
    private ImageView mIv;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("故障反馈");
        findViewById(R.id.loLeft).setOnClickListener(this);
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.loScan).setOnClickListener(this);
        mEtInput = (EditText)findViewById(R.id.etInput);
        mEtPhone = (EditText)findViewById(R.id.etPhone);
        mEtEan = (EditText)findViewById(R.id.etBikeEan);
        mEtEan.setKeyListener(null);//禁止输入
        mEtEan.setOnClickListener(this);
        mTvNum = (TextView)findViewById(R.id.tvNum);
        mIv = (ImageView)findViewById(R.id.ivId1);
        mIv.setOnClickListener(this);
        svProgressHUD = new SVProgressHUD(this);

        mEtInput.addTextChangedListener(mTextWatcher);
        if(mMyData.mCurProblem == null){
            mMyData.mCurProblem = new DProblem();
        }else{
            showProblem();
        }
    }

    void showProblem(){
        if(!MyMethod.isEmpty_null(mMyData.mCurProblem.mDescribe)){
            mEtInput.setText(mMyData.mCurProblem.mDescribe);
        }
        if(!MyMethod.isEmpty_null(mMyData.mCurProblem.mPhone)){
            mEtPhone.setText(mMyData.mCurProblem.mPhone);
        }
        if(!MyMethod.isEmpty_null(mMyData.mCurProblem.mEncode)){
            mEtEan.setText(mMyData.mCurProblem.mEncode);
        }
    }

    void getProblem(){
        mMyData.mCurProblem.mDescribe = mEtInput.getText().toString();
        mMyData.mCurProblem.mPhone = mEtPhone.getText().toString();
        mMyData.mCurProblem.mEncode = mEtEan.getText().toString();
        mMyData.mCurProblem.mUserId = mMyData.mUserId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.btnOK://返回
                if(StringUtil.isEmpty(mEtEan.getText().toString())) {
                    mEtEan.setError("请输入正确格式的车辆编码");
                }else if(StringUtil.isEmpty(mEtInput.getText().toString())) {
                    mEtInput.setError("内容不能为空");
                }else if(!StringUtil.isPhone(mEtPhone.getText().toString())) {
                    mEtPhone.setError("请输入正确格式的电话号码");
                }else {
                    svProgressHUD.show();
                    getProblem();
                    if(mLocalFilePath != null){//没有图片
                        Http.uploadPic(mLocalFilePath, mNewFileName, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 1://成功
                                        mMyData.mCurProblem.mPic = mNewFileName;
                                        Toast.makeText(AcFault.this, "故障图片上传成功", Toast.LENGTH_SHORT).show();
                                        addProblem();
                                        break;
                                    case 0://失败
                                        Toast.makeText(AcFault.this, "故障图片上传失败", Toast.LENGTH_SHORT).show();
                                        svProgressHUD.dismiss();
                                        break;
                                }
                            }
                        });
                    }else if(mBitmap!=null && !mBitmap.isRecycled()){
                        Http.uploadPic(mBitmap, mNewFileName, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 1://成功
                                        mMyData.mCurProblem.mPic = mNewFileName;
                                        Toast.makeText(AcFault.this, "故障图片上传成功", Toast.LENGTH_SHORT).show();
                                        addProblem();
                                        break;
                                    case 0://失败
                                        Toast.makeText(AcFault.this, "故障图片上传失败", Toast.LENGTH_SHORT).show();
                                        svProgressHUD.dismiss();
                                        break;
                                }
                            }
                        });
                    }else{
                        addProblem();
                    }
                }
                break;
            case R.id.etBikeEan:
            case R.id.loScan:
                startActivityForResult(new Intent(this,saiyi.com.xiande.zbar.CaptureActivity.class).putExtra("type",2), 1);
                break;
            case R.id.ivId1:
                PopUpView.showPopwindow(this, new PopUpView.Click() {
                    @Override
                    public void onCamera() {
                        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 3);
                    }
                    @Override
                    public void onAlbum() {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                    @Override
                    public void onCancel() {
                    }
                });
                break;
        }
    }

    private void addProblem(){
        Http.addProblem(mMyData.mCurProblem.toJSONObject(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                switch (msg.what) {
                    case 1://成功
                        Toast.makeText(AcFault.this, "提交成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 0://失败
                        Toast.makeText(AcFault.this, "提交失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            int n = s.length();
            mTvNum.setText("" + (AppConstants.FaultCharNum - n));
            if(n >= AppConstants.FaultCharNum){
                s.delete(AppConstants.FaultCharNum,n);
            }
        }
    };

    private String mNewFileName = null;
    private String mLocalFilePath = null;
    private Bitmap mBitmap;
    private void setPic(String filePath){
        if(mBitmap!=null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
        mLocalFilePath = filePath;
    }
    private void setPic(Bitmap bitmap){
        if(mBitmap!=null && !mBitmap.isRecycled()){
            mBitmap.recycle();
        }
        mLocalFilePath = null;
        mBitmap = bitmap;
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK && requestCode==2) {//选择头像图片成功返回
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = getContentResolver();
            try {
                final Bitmap bitmap = MyMethod.xiandeCompress(BitmapFactory.decodeStream(cr.openInputStream(uri)), this);
                final File file = new File(MyMethod.getRealFilePath(AcFault.this,uri));
                Date date = new Date();
                mNewFileName = mMyData.mUserId + Http.CXProblemPic + MyMethod.DateToString(date) + "." + MyMethod.getSuffix(file);
                mIv.setImageBitmap(bitmap);
                setPic(MyMethod.getRealFilePath(AcFault.this, uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == 3) {//头像拍照成功返回
            final Bitmap bitmap = MyMethod.xiandeCompress((Bitmap)data.getExtras().get("data"),this);
            Date date = new Date();
            mNewFileName = mMyData.mUserId + Http.CXProblemPic + MyMethod.DateToString(date) + ".jpg";
            mIv.setImageBitmap(bitmap);
            setPic(bitmap);
        }
        if(resultCode == 1) {//成功返回
            switch (requestCode) {
                case 1://扫码返回
                    final String result = data.getStringExtra("result");
                    if(MyMethod.isBikeEanCode(result)) {
                        mEtEan.setError(null);
                        mEtEan.setText(result);
                    } else {
                        Toast.makeText(this,"二维码格式错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
