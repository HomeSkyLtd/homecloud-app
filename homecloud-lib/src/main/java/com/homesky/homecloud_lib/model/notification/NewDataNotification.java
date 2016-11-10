package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewDataNotification implements Serializable, Notification {
    private static final String TAG = "NewDataNotif";

    List<NodeData> mData;

    public NewDataNotification(List<NodeData> data){
        mData = data;
    }

    public static NewDataNotification from(String jsonStr) {
        List<NodeData> data = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray dataJson = obj.getJSONArray(Constants.Fields.NewDataNotification.DATA);
            for(int i = 0 ; i < dataJson.length() ; ++i){
                JSONObject dataJsonObj = dataJson.getJSONObject(i);
                int nodeId = dataJsonObj.getInt(Constants.Fields.NewDataNotification.NODE_ID);
                int dataId = dataJsonObj.getInt(Constants.Fields.NewDataNotification.DATA_ID);
                BigDecimal value = new BigDecimal(dataJsonObj.getString(Constants.Fields.NewDataNotification.NODE_ID));
                data.add(new NodeData(nodeId, dataId, value));
            }
            return new NewDataNotification(data);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    public static class NodeData{
        private int mNodeId, mDataId;
        private BigDecimal mValue;

        public NodeData(int nodeId, int dataId, BigDecimal value) {
            this.mNodeId = nodeId;
            this.mDataId = dataId;
            this.mValue = value;
        }

        public int getNodeId() {
            return mNodeId;
        }

        public int getDataId() {
            return mDataId;
        }

        public BigDecimal getValue() {
            return mValue;
        }
    }
}
