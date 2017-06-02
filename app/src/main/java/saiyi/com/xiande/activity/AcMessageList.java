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

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DMessage;
import saiyi.com.xiande.data.MyData;

public class AcMessageList extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;
    private ListView mLv;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("使用指南");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mLv = (ListView)findViewById(R.id.lv);

        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.show();
        Http.getMsgList(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                if (msg.what == 1) {
                    if(msg.obj != null) {
                        mMyData.mMsgList = DMessage.fromJSONArray((JSONArray) msg.obj);
                        mMsgAdapter = new MsgAdapter(AcMessageList.this, toMapList(mMyData.mMsgList), R.layout.item_msglist, new String[]{}, new int[]{});
                        mLv.setAdapter(mMsgAdapter);
                    }
                }
            }
        });

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AcMessageList.this, AcMessage.class).putExtra("index",position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
        }
    }

    private MsgAdapter mMsgAdapter;
    private class MsgAdapter extends SimpleAdapter {
        private List<Map<String,DMessage>> mListViewData = new ArrayList<>();//电动车列表（适配后的数据）

        public MsgAdapter(Context context, List<Map<String,DMessage>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mListViewData = data;
        }
        public void AddData(List<Map<String,DMessage>> data){
            mListViewData.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);
            DMessage message = mListViewData.get(position).get("index");
            ImageView iconView = (ImageView) v.findViewById(R.id.avatar_iv);

            ((TextView) v.findViewById(R.id.tvTitle)).setText(message.mMsgTitle);
            ((TextView) v.findViewById(R.id.tvTime)).setText(MyMethod.DateToString_mmdd(message.mMsgTime));
            ((TextView) v.findViewById(R.id.tvText)).setText(message.mMsgContent);

            return v;
        }
    }

    public  List<Map<String,DMessage>> toMapList(List<DMessage> list){
        if(list == null){
            return null;
        }
        List<Map<String,DMessage>> mapList = new ArrayList<>(list.size());
        for(DMessage object:list){
            Map<String,DMessage> map = new HashMap<>(1);
            map.put("index",object);
            mapList.add(map);
        }
        return mapList;
    }
}
