package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.util.Map;

public class SetNodeExtraRequest extends RequestModel {
    Map<String, String> mExtra;
    private int mNodeId;
    private String mControllerId;

    public SetNodeExtraRequest(Map<String, String> extra, int nodeId, String controllerId){
        super(Constants.Values.Functions.SET_NODE_EXTRA);
        mExtra = extra;
        mNodeId = nodeId;
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.SetNodeExtra.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.SetNodeExtra.CONTROLLER_ID).value(mControllerId);
        writer.name(Constants.Fields.SetNodeExtra.EXTRA);
        writer.beginObject();
        for(String key : mExtra.keySet()){
            writer.name(key).value(mExtra.get(key));
        }
        writer.endObject();
    }
}
