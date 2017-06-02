package saiyi.com.xiande.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Common;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.base.PickerWindow;
import saiyi.com.xiande.base.PopUpView;
import saiyi.com.xiande.data.DStudent;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.PreferencesUtils;
import saiyi.com.xiande.utils.StringUtil;

import static saiyi.com.xiande.R.id.tv_identity_num;

public class AcUserInfo extends BaseActivity implements View.OnClickListener {
    private ImageView mIvAvatar;
    private ImageView mIvId1;
    private ImageView mIvId2;
    private ImageView mIvSid;
    private EditText mEtName;
    private TextView mEtSchool;
    private MyData mMyData;
    private Button mBtnOK;
    private TextView mIdentityNumTv;
    private TextView mStudentNumTv;
    private SVProgressHUD svProgressHUD;
    //学校选择器
    private PickerWindow mSchoolPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        mSchoolPicker = new PickerWindow(this);
        mMyData = BaseApplication.getInstance().getMyData();
        if(mMyData.mStudent == null) {
            mMyData.mStudent = new DStudent();
        }
        mMyData.mUserId = getIntent().getStringExtra("phone");
        ((TextView)findViewById(R.id.tvTitle)).setText("个人信息认证");
        findViewById(R.id.loLeft).setOnClickListener(this);

        mBtnOK = (Button)findViewById(R.id.btn_user_info_OK);
        mBtnOK.setOnClickListener(this);
        svProgressHUD = new SVProgressHUD(this);

        mIvAvatar = (ImageView)findViewById(R.id.ivAvatar);
        mIvAvatar.setOnClickListener(this);
        mIvId1 = (ImageView)findViewById(R.id.ivId1);
        mIvId1.setOnClickListener(this);
        mIvId2 = (ImageView)findViewById(R.id.ivId2);
        mIvId2.setOnClickListener(this);
        mIvSid = (ImageView)findViewById(R.id.ivSd1);
        mIvSid.setOnClickListener(this);

        mEtName = (EditText)findViewById(R.id.etName);
        mEtSchool = (TextView) findViewById(R.id.etSchool);

        mEtName.addTextChangedListener(mTextWatcher);
        mEtSchool.addTextChangedListener(mTextWatcher);
        mEtSchool.setOnClickListener(this);

        mIdentityNumTv = (TextView) findViewById(R.id.tv_identity_num);
        mStudentNumTv = (TextView) findViewById(R.id.tv_student_num);

        mMyData.mStudent.mHeadPic = null;
        mMyData.mStudent.mIdFace = null;
        mMyData.mStudent.mIdBack = null;
        mMyData.mStudent.mSidPic = null;
//        if(!MyMethod.isEmpty_null(mMyData.mStudent.mHeadPic)) {
//            Picasso.with(this).load(Http.UrlDownload + mMyData.mStudent.mHeadPic).resize(80, 80).into(mIvAvatar);
//        }
//        if(!MyMethod.isEmpty_null(mMyData.mStudent.mIdFace)) {
//            Picasso.with(this).load(Http.UrlDownload + mMyData.mStudent.mIdFace).resize(80, 80).into(mIvId1);
//        }
//        if(!MyMethod.isEmpty_null(mMyData.mStudent.mIdBack)) {
//            Picasso.with(this).load(Http.UrlDownload + mMyData.mStudent.mIdBack).resize(80, 80).into(mIvId2);
//        }
//        if(!MyMethod.isEmpty_null(mMyData.mStudent.mSidPic)) {
//            Picasso.with(this).load(Http.UrlDownload + mMyData.mStudent.mSidPic).resize(80, 80).into(mIvSid);
//        }

        mSchoolPicker.setOnClickOkListener(new PickerWindow.OnClickOkListener() {
            @Override
            public void onClickOk(String text) {
                mEtSchool.setText(text);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mMyData.mStudent != null) {
            showInfo(mMyData.mStudent);
        }
    }

    void showInfo(DStudent student){
        if(student.mName!=null && !student.mName.equals("null")) {
            mEtName.setText(student.mName);
        }
        if(student.mSchool!=null && !student.mSchool.equals("null")) {
            mEtSchool.setText(student.mSchool);
        }
    }

    void setInfo(DStudent student){
        student.mName = mEtName.getText().toString();
        student.mSchool = mEtSchool.getText().toString();
        student.mIsValid = 1;
    }

    private void getPic(final int type){
        PopUpView.showPopwindow(this, new PopUpView.Click() {
            @Override
            public void onCamera() {
                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 10+type);
            }
            @Override
            public void onAlbum() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, type);
            }
            @Override
            public void onCancel() {}
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                startActivity(new Intent(this, Login.class));
                break;
            //选择学校
            case R.id.etSchool:
                mSchoolPicker.popWindow(v);
                break;
            case R.id.ivAvatar://头像
                getPic(1);
                break;
            case R.id.ivId1://身份证1
                getPic(2);
                break;
            case R.id.ivId2://身份证2
                getPic(3);
                break;
            case R.id.ivSd1://学生证
                getPic(4);
                break;
            case R.id.btn_user_info_OK://提交
                if(!isHavePic(2) && StringUtil.isEmpty(mMyData.mStudent.mIdFace)){
                    Toast.makeText(this, "请选择身份证正面照片", Toast.LENGTH_SHORT).show();
                }else if(!isHavePic(3) && StringUtil.isEmpty(mMyData.mStudent.mIdBack)){
                    Toast.makeText(this, "请选择身份证背面照片", Toast.LENGTH_SHORT).show();
                }else if(!isHavePic(4) && StringUtil.isEmpty(mMyData.mStudent.mSidPic)){
                    Toast.makeText(this, "请选择学生证照片", Toast.LENGTH_SHORT).show();
                }else {
                    mBtnOK.setEnabled(false);
                    setInfo(mMyData.mStudent);
                    relateCount = 0;
                    if(!TextUtils.isEmpty( mMyData.mStudent.mHeadPic)) {
                        uploadPic(1);//上传头像
                    } else {
                        eanRelateFinishNum = 4;
                    }
                    uploadPic(2);//上传身份证正面
                    uploadPic(3);//上传身份证背面
                    uploadPic(4);//上传学生证
                    svProgressHUD.show();
                    if(mMyData.mUserId == null) {
                        mMyData.mUserId = mMyData.mStudent.mUserId;
                    }
                    mMyData.mStudent.mIsValid = 1;
                    Http.updateUserInfo(mMyData.mUserId,mMyData.mStudent, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mBtnOK.setEnabled(true);
                            switch (msg.what) {
                                case 1://成功
                                    mMyData.mStudent.mIsValid = 1;//已提交
                                    break;
                                case 0://失败
                                    mMyData.mStudent.mIsValid = 0;//初始状态
                                    svProgressHUD.dismiss();
                                    Toast.makeText(AcUserInfo.this, "提交失败", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            checkRelateFinish();
                        }
                    });
                }
                break;
        }
    }

    private void checkInputError(){
        mBtnOK.setEnabled(false);
        if(StringUtil.isEmpty(String.valueOf(mEtName.getText()))){
            return;
        }
        if(StringUtil.isEmpty(String.valueOf(mEtSchool.getText()))){
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

    //同一张图片，只能有 Bitmap 或者 LocalFilePath
    private Bitmap[] mBmAvatarArr = new Bitmap[4];//照相返回的，头像、身份证正面、身份证背面、学生证
    private String[] LocalFilePath = new String[4];//选择图片返回的，头像、身份证正面、身份证背面、学生证
    private void setPic(Bitmap bitmap,int type){
        if(mBmAvatarArr[type-1]!=null && !mBmAvatarArr[type-1].isRecycled()){
            mBmAvatarArr[type-1].recycle();
        }
        if(LocalFilePath[type-1] != null){
            LocalFilePath[type-1] = null;
        }
        mBmAvatarArr[type-1] = bitmap;
        setSelectPicNum(type);
    }

    private void setSelectPicNum(int type) {
        if(type == 2) {
            mIdentityNumTv.setText("1/2");
        } else if(type == 3) {
            mIdentityNumTv.setText("2/2");
        } else if(type == 4) {
            mStudentNumTv.setText("1/1");
        }
    }
    private void setPic(String filePath,int type){
        if(mBmAvatarArr[type-1]!=null && !mBmAvatarArr[type-1].isRecycled()){
            mBmAvatarArr[type-1].recycle();
            mBmAvatarArr[type-1] = null;
        }
        LocalFilePath[type-1] = filePath;
        setSelectPicNum(type);
    }
    private boolean isHavePic(int type){
        if(StringUtil.isEmpty(LocalFilePath[type-1]) && (mBmAvatarArr[type-1]==null || mBmAvatarArr[type-1].isRecycled())){
            return false;
        }else{
            return true;
        }
    }

    private final String[] PicNameArr = {Http.CXHeadPic,Http.CXCardPicFace,Http.CXCardPicBack,Http.CXStudentPic};
    private void uploadPic(final int type) {
        String filePath = LocalFilePath[type - 1];
        if (!MyMethod.isEmpty(filePath)) {
            clearCache(type);
            final String newFileName = mMyData.mUserId + PicNameArr[type - 1] + "." + MyMethod.getSuffix(filePath);
            Http.uploadPic(filePath, newFileName, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    checkRelateFinish();
                    switch (msg.what) {
                        case 1://成功
                            //Toast.makeText(AcUserInfo.this, "图片" + type + "上传成功", Toast.LENGTH_SHORT).show();
                            break;
                        case 0://失败
                            //Toast.makeText(AcUserInfo.this, "图片" + type + "上传失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else if(mBmAvatarArr[type-1]!=null && !mBmAvatarArr[type-1].isRecycled()) {
            clearCache(type);
            final String newFileName = mMyData.mUserId + PicNameArr[type - 1] + ".jpg";
            Http.uploadPic(mBmAvatarArr[type-1], newFileName, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    checkRelateFinish();
                    switch (msg.what) {
                        case 1://成功
                            //Toast.makeText(AcUserInfo.this, "图片" + type + "上传成功", Toast.LENGTH_SHORT).show();
                            break;
                        case 0://失败
                            //Toast.makeText(AcUserInfo.this, "图片" + type + "上传失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else {
            checkRelateFinish();
        }
    }
    private int eanRelateFinishNum = 5;//5个线程同步.
    private int relateCount;//用于多线程同步的计数器。
    private synchronized void incRelateCount(){
        relateCount++;
    }
    private void checkRelateFinish(){//计数器增加及检查结束
        incRelateCount();
        if(relateCount >= eanRelateFinishNum){//eanRelateFinishNum 个线程都执行完时，就是已经执行完了
            svProgressHUD.dismiss();
            if(mMyData.mStudent.mIsValid == 1) {
                startActivityForResult(new Intent(AcUserInfo.this, AcUserInfoSubmit.class), 5);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == 5){//提交成功返回的
                finish();
            }else if(requestCode>=1 && requestCode<= 4){//选择图片返回
                Uri uri = data.getData();
                try {
                    final Bitmap bitmap = getImage(uri);
                    final File file = new File(MyMethod.getRealFilePath(AcUserInfo.this,uri));
                    String newFileName = mMyData.mUserId + PicNameArr[requestCode-1] + "." + MyMethod.getSuffix(file);
                    showPic(bitmap,newFileName,requestCode);
                    setPic(MyMethod.getRealFilePath(AcUserInfo.this,uri),requestCode);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }else if(requestCode>=11 && requestCode<=14){//照相返回
                int type = requestCode-10;
                final Bitmap bitmap = MyMethod.xiandeCompress((Bitmap) data.getExtras().get("data"), this);
                String newFileName = mMyData.mUserId + PicNameArr[type-1] + ".jpg";
                showPic(bitmap,newFileName,type);
                setPic(bitmap,type);
            }
        }
    }

    private Bitmap getImage(Uri uri){
        Bitmap bitmap = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            // 实例化一个Options对象
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            //指定它只读取图片的信息而不加载整个图片
            opts.inJustDecodeBounds = true;
            // 通过这个Options对象，从输入流中读取图片的信息
            BitmapFactory.decodeStream(is, null, opts);
            final int bitmapWidth = opts.outWidth;
            final int bitmapHeight = opts.outHeight;
            // 分析图片的宽高比，用于进行优化
            if (bitmapHeight > getWindowManager().getDefaultDisplay().getHeight() || bitmapWidth > getWindowManager().getDefaultDisplay().getWidth()) {
                final int scaleX = bitmapWidth / getWindowManager().getDefaultDisplay().getWidth();
                final int scaleY = bitmapHeight / getWindowManager().getDefaultDisplay().getHeight();
                if (scaleX > scaleY) {
                    opts.inSampleSize = scaleX;
                } else {
                    opts.inSampleSize = scaleY;
                }
            } else {
                opts.inSampleSize = 1;
            }
            // 设定读取完整的图片信息
            opts.inJustDecodeBounds = false;
            is = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception e) {
        }catch (OutOfMemoryError outOfMemoryError){
        }
        return bitmap;
    }

    private void showPic(Bitmap bitmap,String newFileName,int type){
        switch (type) {
            case 1:
                mIvAvatar.setImageBitmap(bitmap);
                mMyData.mStudent.mHeadPic = newFileName;
                break;
            case 2:
                mIvId1.setImageBitmap(bitmap);
                mMyData.mStudent.mIdFace = newFileName;
                break;
            case 3:
                mIvId2.setImageBitmap(bitmap);
                mMyData.mStudent.mIdBack = newFileName;
                break;
            case 4:
                mIvSid.setImageBitmap(bitmap);
                mMyData.mStudent.mSidPic = newFileName;
                break;
        }
    }

    private void clearCache(int type){
        switch (type) {
            case 1:
                if(!MyMethod.isEmpty_null(mMyData.mStudent.mHeadPic)){
                    Picasso.with(this).invalidate(Http.UrlDownload + mMyData.mStudent.mHeadPic);
                }
                break;
            case 2:
                if(!MyMethod.isEmpty_null(mMyData.mStudent.mIdFace)){
                    Picasso.with(this).invalidate(Http.UrlDownload + mMyData.mStudent.mIdFace);
                }
                break;
            case 3:
                if(!MyMethod.isEmpty_null(mMyData.mStudent.mIdBack)){
                    Picasso.with(this).invalidate(Http.UrlDownload + mMyData.mStudent.mIdBack);
                }
                break;
            case 4:
                if(!MyMethod.isEmpty_null(mMyData.mStudent.mSidPic)){
                    Picasso.with(this).invalidate(Http.UrlDownload + mMyData.mStudent.mSidPic);
                }
                break;
        }
    }
}
