package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 商家设置的标准
 */
public class DDict {
    public double mBatteryAlarm;//电量报警值
    public double mBalanceAlarm;//余额报警值
    public double mOverrunsAlarm;//超支报警
    public double mDeposit;//押金金额
    public int mUnusedTime;//连续多少天未使用

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("batteryAlarm",mBatteryAlarm);} catch (JSONException ignored) {}
        try {jsonObject.put("balanceAlarm",mBalanceAlarm);} catch (JSONException ignored) {}
        try {jsonObject.put("overrunsAlarm",mOverrunsAlarm);} catch (JSONException ignored) {}
        try {jsonObject.put("deposit",mDeposit);} catch (JSONException ignored) {}
        try {jsonObject.put("unusedTime",mUnusedTime);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DDict fromJSONObject(JSONObject jsonObject){
        DDict dict = new DDict();
        if(jsonObject != null){

            try {dict.mBatteryAlarm = MyMethod.parseDouble(jsonObject.getString("batteryAlarm"));} catch (JSONException ignored) {}
            try {dict.mBalanceAlarm = MyMethod.parseDouble(jsonObject.getString("balanceAlarm"));} catch (JSONException ignored) {}
            try {dict.mOverrunsAlarm = MyMethod.parseDouble(jsonObject.getString("overrunsAlarm"));} catch (JSONException ignored) {}
            try {dict.mDeposit = MyMethod.parseDouble(jsonObject.getString("deposit"));} catch (JSONException ignored) {}
            try {dict.mUnusedTime = MyMethod.parseInt(jsonObject.getString("unusedTime"));} catch (JSONException ignored) {}

        }
        return dict;
    }

    public static List<DDict> fromJSONArray(JSONArray jsonArray){
        List<DDict> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); ++i){
            try {
                list.add(fromJSONObject(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
