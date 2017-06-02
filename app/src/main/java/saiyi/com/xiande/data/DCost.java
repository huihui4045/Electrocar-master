package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 花费
 */
public class DCost {
    public String mUserId;//学生账号
    public double mCost;//消费金额
    public Date mCostTime;//消费时间
    public String mDesc;//描述信息

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("cost",mCost);} catch (JSONException ignored) {}
        try {jsonObject.put("costTime",mCostTime);} catch (JSONException ignored) {}
        try {jsonObject.put("description",mDesc);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DCost fromJSONObject(JSONObject jsonObject){
        DCost dCost = new DCost();
        if(jsonObject != null){

            try {dCost.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dCost.mCost = MyMethod.parseDouble(jsonObject.getString("cost"));} catch (JSONException ignored) {}
            try {dCost.mCostTime = MyMethod.getDateFromString(jsonObject.getString("costTime"));} catch (JSONException ignored) {}
            try {dCost.mDesc = jsonObject.getString("description");} catch (JSONException ignored) {}

        }
        return dCost;
    }

    public static List<DCost> fromJSONArray(JSONArray jsonArray){
        List<DCost> list = new ArrayList<>();
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
