package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 账户信息
 */
public class DAccount {
    public String mUserId;//学生账号
    public double mTotalAmount;//当前总额 = 可用余额 + 押金;
    public double mBalance;//可用余额
    public double mDeposit;//押金

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("totalAmount",mTotalAmount);} catch (JSONException ignored) {}
        try {jsonObject.put("balance",mBalance);} catch (JSONException ignored) {}
        try {jsonObject.put("deposit",mDeposit);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DAccount fromJSONObject(JSONObject jsonObject){
        DAccount data = new DAccount();
        if(jsonObject != null){

            try {data.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {data.mTotalAmount = MyMethod.parseDouble(jsonObject.getString("totalAmount"));} catch (JSONException ignored) {}
            try {data.mBalance = MyMethod.parseDouble(jsonObject.getString("balance"));} catch (JSONException ignored) {}
            try {data.mDeposit = MyMethod.parseDouble(jsonObject.getString("deposit"));} catch (JSONException ignored) {}
        }
        return data;
    }

    public static List<DAccount> fromJSONArray(JSONArray jsonArray){
        List<DAccount> list = new ArrayList<>();
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
