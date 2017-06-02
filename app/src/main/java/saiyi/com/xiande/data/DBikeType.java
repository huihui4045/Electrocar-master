package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 车辆类型
 */
public class DBikeType {
    public String mTypeId;//车辆类型Id
    public String mTypeName;//车辆类型名称
    public double mPrice;//出租价格
    public String mPicPath;//车辆类型图片路径

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("carTypeId",mTypeId);} catch (JSONException ignored) {}
        try {jsonObject.put("carTypeName",mTypeName);} catch (JSONException ignored) {}
        try {jsonObject.put("price",mPrice);} catch (JSONException ignored) {}
        try {jsonObject.put("carImg",mPicPath);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DBikeType fromJSONObject(JSONObject jsonObject){
        DBikeType dBikeType = new DBikeType();
        if(jsonObject != null){

            try {dBikeType.mTypeId = jsonObject.getString("carTypeId");} catch (JSONException ignored) {}
            try {dBikeType.mTypeName = jsonObject.getString("carTypeName");} catch (JSONException ignored) {}
            try {dBikeType.mPrice = MyMethod.parseDouble(jsonObject.getString("price"));} catch (JSONException ignored) {}
            try {dBikeType.mPicPath = jsonObject.getString("carImg");} catch (JSONException ignored) {}

        }
        return dBikeType;
    }

    public static List<DBikeType> fromJSONArray(JSONArray jsonArray){
        List<DBikeType> list = new ArrayList<>();
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
