package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 推送消息
 */
public class DMessage {
    public String mMsgId;//消息ID
    public String mMsgTitle;//消息标题
    public String mMsgContent;//消息内容
    public Date mMsgTime;//推送时间

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("msgId",mMsgId);} catch (JSONException ignored) {}
        try {jsonObject.put("msgTitle",mMsgTitle);} catch (JSONException ignored) {}
        try {jsonObject.put("msgContent",mMsgContent);} catch (JSONException ignored) {}
        try {jsonObject.put("msgTime",mMsgTime);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DMessage fromJSONObject(JSONObject jsonObject){
        DMessage dMessage = new DMessage();
        if(jsonObject != null){

            try {dMessage.mMsgId = jsonObject.getString("msgId");} catch (JSONException ignored) {}
            try {dMessage.mMsgTitle = jsonObject.getString("msgTitle");} catch (JSONException ignored) {}
            try {dMessage.mMsgContent = jsonObject.getString("msgContent");} catch (JSONException ignored) {}
            try {dMessage.mMsgTime = MyMethod.getDateFromString(jsonObject.getString("msgTime"));} catch (JSONException ignored) {}

        }
        return dMessage;
    }

    public static List<DMessage> fromJSONArray(JSONArray jsonArray){
        List<DMessage> list = new ArrayList<>();
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
