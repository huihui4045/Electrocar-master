package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 停车场信息
 */
public class DStopPosition {
    public String mId;
    public double mLongitude;
    public double mLatitud;
    public String mName;//校名
    public String mSName;//停车点名字
    public int mParkingNum;//车位数量
    public String mAddress;
    public double mRadius;//电子围栏的半径

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("stopId",mId);} catch (JSONException ignored) {}
        try {jsonObject.put("stopLongitude",mLongitude);} catch (JSONException ignored) {}
        try {jsonObject.put("stopLatitud",mLatitud);} catch (JSONException ignored) {}
        try {jsonObject.put("name",mName);} catch (JSONException ignored) {}
        try {jsonObject.put("sname",mSName);} catch (JSONException ignored) {}
        try {jsonObject.put("carCount",mParkingNum);} catch (JSONException ignored) {}
        try {jsonObject.put("address",mAddress);} catch (JSONException ignored) {}
        try {jsonObject.put("radius",mRadius);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DStopPosition fromJSONObject(JSONObject jsonObject){
        DStopPosition dStopPosition = new DStopPosition();
        if(jsonObject != null){

            try {dStopPosition.mId = jsonObject.getString("stopId");} catch (JSONException ignored) {}
            try {dStopPosition.mLongitude = MyMethod.parseDouble(jsonObject.getString("stopLongitude"));} catch (JSONException ignored) {}
            try {dStopPosition.mLatitud = MyMethod.parseDouble(jsonObject.getString("stopLatitud"));} catch (JSONException ignored) {}
            try {dStopPosition.mName = jsonObject.getString("name");} catch (JSONException ignored) {}
            try {dStopPosition.mSName = jsonObject.getString("sname");} catch (JSONException ignored) {}
            try {dStopPosition.mParkingNum = MyMethod.parseInt(jsonObject.getString("carCount"));} catch (JSONException ignored) {}
            try {dStopPosition.mAddress = jsonObject.getString("address");} catch (JSONException ignored) {}
            try {dStopPosition.mRadius = MyMethod.parseDouble(jsonObject.getString("radius"));} catch (JSONException ignored) {}

        }
        return dStopPosition;
    }

    public static List<DStopPosition> fromJSONArray(JSONArray jsonArray){
        List<DStopPosition> list = new ArrayList<>();
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
