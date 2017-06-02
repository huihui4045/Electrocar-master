package saiyi.com.xiande.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DCost;
import saiyi.com.xiande.data.MyData;

public class AcPayRecord extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;
    private TextView mTvSum;
    private ListView mLv;
    private SVProgressHUD svProgressHUD;

    private LinearLayout mLoLv;
    private ImageView mIvMsg;
    private TextView mTvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payrecord);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("收支明细");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mLv = (ListView)findViewById(R.id.lv);
        mTvSum = (TextView)findViewById(R.id.tvSum);

        mLoLv = (LinearLayout)findViewById(R.id.loLv);
        mLoLv.setVisibility(View.INVISIBLE);
        mIvMsg = (ImageView)findViewById(R.id.ivMsg);
        mTvMsg = (TextView)findViewById(R.id.tvMsg);

        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.show();
        Http.getCostList(mMyData.mUserId,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                if(msg.what == 1){//成功
                    if(msg.obj != null) {
                        mMyData.mCostList = DCost.fromJSONArray((JSONArray) msg.obj);
                        if (mMyData.mCostList != null && !mMyData.mCostList.isEmpty()) {
                            mLv.setAdapter(new PayRecordAdapter(AcPayRecord.this, toMapList(mMyData.mCostList), R.layout.item_payrecord));
                            mLoLv.setVisibility(View.VISIBLE);
                            mIvMsg.setVisibility(View.INVISIBLE);
                            mTvMsg.setVisibility(View.INVISIBLE);
                        }
                        Http.getCostSum(mMyData.mUserId, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 1) {//成功
                                    if(msg.obj != null) {
                                        try {
                                            JSONObject jsonObject = ((JSONArray) msg.obj).getJSONObject(0);
                                            mTvSum.setText("已入账单共" + jsonObject.get("countPrice") + "笔，" +
                                                    (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月支出共" + jsonObject.get("sumPrice") + "元");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {//失败
                                }
                            }
                        });
                    }
                }else{
                    Toast.makeText(AcPayRecord.this, "查询收支明细失败", Toast.LENGTH_SHORT).show();
                }

                if(mMyData.mCostList==null || mMyData.mCostList.isEmpty()) {
                    mLoLv.setVisibility(View.INVISIBLE);
                    mIvMsg.setVisibility(View.VISIBLE);
                    mTvMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private class PayRecordAdapter extends SimpleAdapter {
        private List<Map<String,DCost>> mListViewData = new ArrayList<>();//电动车列表（适配后的数据）

        public PayRecordAdapter(Context context, List<Map<String,DCost>> data, int resource) {
            super(context, data, resource, new String[]{}, new int[]{});
            mListViewData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);
            DCost cost = mListViewData.get(position).get("index");

            ((TextView) v.findViewById(R.id.tvName)).setText(cost.mDesc);
            ((TextView) v.findViewById(R.id.tvDate)).setText(MyMethod.DateToString(cost.mCostTime));
            ((TextView) v.findViewById(R.id.tvMoney)).setText("" + cost.mCost);

            return v;
        }
    }

    public  List<Map<String,DCost>> toMapList(List<DCost> list){
        if(list == null){
            return null;
        }
        List<Map<String,DCost>> mapList = new ArrayList<>(list.size());
        for(DCost object:list){
            Map<String,DCost> map = new HashMap<>(1);
            map.put("index",object);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
        }
    }
}
