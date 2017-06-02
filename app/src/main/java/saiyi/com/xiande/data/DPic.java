package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生身份资料
 */
public class DPic {
    public String mUserId;
    public String mHeadPic;//头像
    public String mIdFace;//身份证正面
    public String mIdBack;//身份证背面
    public String mSidPic;//学生证

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("headPic",mHeadPic);} catch (JSONException ignored) {}
        try {jsonObject.put("cardPicFace",mIdFace);} catch (JSONException ignored) {}
        try {jsonObject.put("cardPicBack",mIdBack);} catch (JSONException ignored) {}
        try {jsonObject.put("studentPic",mSidPic);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DPic fromJSONObject(JSONObject jsonObject){
        DPic dPic = new DPic();
        if(jsonObject != null){

            try {dPic.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dPic.mHeadPic = jsonObject.getString("headPic");} catch (JSONException ignored) {}
            try {dPic.mIdFace = jsonObject.getString("cardPicFace");} catch (JSONException ignored) {}
            try {dPic.mIdBack = jsonObject.getString("cardPicBack");} catch (JSONException ignored) {}
            try {dPic.mSidPic = jsonObject.getString("studentPic");} catch (JSONException ignored) {}

        }
        return dPic;
    }

    public static List<DPic> fromJSONArray(JSONArray jsonArray){
        List<DPic> list = new ArrayList<>();
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
