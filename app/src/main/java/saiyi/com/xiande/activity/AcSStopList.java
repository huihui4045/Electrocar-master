package saiyi.com.xiande.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

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
import saiyi.com.xiande.data.DStopPosition;
import saiyi.com.xiande.data.MyData;

/**
 * 商家登录，停车场列表
 */
public class AcSStopList extends BaseActivity implements View.OnClickListener{
    private SVProgressHUD svProgressHUD;
    private TextView mTvRight;
    private ListView mLvStopList;
    private MyData mMyData;
    private List<DStopPosition> mStopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_bikelist);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView) findViewById(R.id.tvTitle)).setText("停车场");
        findViewById(R.id.loLeft).setOnClickListener(this);

        mLvStopList = (ListView)findViewById(R.id.lvBikeTypeList);
        svProgressHUD = new SVProgressHUD(this);

        svProgressHUD.show();
        Http.getStopList(mMyData.mUserId, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                switch (msg.what) {
                    case 1:
                        if (msg.obj != null) {
                            mStopList = DStopPosition.fromJSONArray((JSONArray) msg.obj);
                            mLvStopList.setAdapter(new BikeTypeAdapter(AcSStopList.this, getData(mStopList),
                                    R.layout.item_stoplist, new String[]{}, new int[]{}));
                            //((BikeTypeAdapter) mLvBikeTypeList.getAdapter()).notifyDataSetChanged();
                        }
                        break;
                    case 0:
                        Toast.makeText(AcSStopList.this, "查询停车场失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mLvStopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMyData.msChooseStop = mStopList.get(position);
                startActivity(new Intent(AcSStopList.this, AcSCode.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.tvRight:
                Common.showLogoutDialog(this);
                break;
        }
    }

    private class BikeTypeAdapter extends SimpleAdapter {
        private List<Map<String,DStopPosition>> mListViewData = new ArrayList<>();//电动车列表（适配后的数据）

        public BikeTypeAdapter(Context context, List<Map<String,DStopPosition>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mListViewData = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);
            ((TextView) v.findViewById(R.id.tvStopName)).setText(mListViewData.get(position).get("name").mSName);
            return v;
        }
    }

    private List<Map<String,DStopPosition>> getData(List<DStopPosition> list){
        List<Map<String,DStopPosition>> mapList = new ArrayList<>();
        for(DStopPosition stopPosition:list){
            Map<String,DStopPosition> map = new HashMap<>(1);
            map.put("name",stopPosition);
            mapList.add(map);
        }
        return mapList;
    }
}
