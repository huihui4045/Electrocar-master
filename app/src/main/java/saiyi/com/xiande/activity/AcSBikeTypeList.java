package saiyi.com.xiande.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Common;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DBikeType;
import saiyi.com.xiande.data.MyData;

/**
 * 商户登录，车辆类型列表
 */
public class AcSBikeTypeList extends BaseActivity implements View.OnClickListener{
    private SVProgressHUD svProgressHUD;
    private TextView mTvRight;
    private ListView mLvBikeTypeList;
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_bikelist);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView) findViewById(R.id.tvTitle)).setText("车辆类型");
        findViewById(R.id.ivLeft).setVisibility(View.GONE);

        mTvRight = (TextView) findViewById(R.id.tvRight);
        mTvRight.setText("退出登录");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(this);

        mLvBikeTypeList = (ListView)findViewById(R.id.lvBikeTypeList);
        svProgressHUD = new SVProgressHUD(this);

        svProgressHUD.show();
        Http.queryAllBikeType(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                switch (msg.what) {
                    case 1:
                        if (msg.obj != null) {
                            mMyData.mBikeTypeList = DBikeType.fromJSONArray((JSONArray) msg.obj);
                            mLvBikeTypeList.setAdapter(new BikeTypeAdapter(AcSBikeTypeList.this, getData(mMyData.mBikeTypeList),
                                    R.layout.item_biketypelist, new String[]{} , new int[]{} ));
                            //((BikeTypeAdapter) mLvBikeTypeList.getAdapter()).notifyDataSetChanged();
                        }
                        break;
                    case 0:
                        Toast.makeText(AcSBikeTypeList.this, "查询车辆类型失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mLvBikeTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMyData.msChooseBikeType = mMyData.mBikeTypeList.get(position);
                startActivity(new Intent(AcSBikeTypeList.this, AcSStopList.class));
            }
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
            case R.id.tvRight:
                Common.showLogoutDialog(this);
                break;
        }
    }

    private class BikeTypeAdapter extends SimpleAdapter {
        private List<Map<String,DBikeType>> mListViewData = new ArrayList<>();//电动车列表（适配后的数据）

        public BikeTypeAdapter(Context context, List<Map<String,DBikeType>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mListViewData = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);

            ImageView iconView = (ImageView) v.findViewById(R.id.avatar_iv);
            String picPath = mListViewData.get(position).get("type").mPicPath;
            if(!MyMethod.isEmpty_null(picPath)) {
                //Picasso.with(this).invalidate(mMyData.mStudent.mHeadPic);//清除缓存，似乎不用也可以
                Picasso.with(AcSBikeTypeList.this).load(Http.UrlDownload + picPath).resize(80, 80).into(iconView);
            }
            ((TextView) v.findViewById(R.id.tvBikeName)).setText(mListViewData.get(position).get("type").mTypeName);

            return v;
        }
    }

    private List<Map<String,DBikeType>> getData(List<DBikeType> list){
        List<Map<String,DBikeType>> mapList = new ArrayList<>();
        for(DBikeType bikeType:list){
            Map<String,DBikeType> map = new HashMap<>(1);
            map.put("type",bikeType);
            mapList.add(map);
        }
        return mapList;
    }
}
