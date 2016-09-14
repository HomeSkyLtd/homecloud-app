package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.util.Map;

/**
 * Request representing the setNodeExtra function, as defined in the HomeCloud protocol.
 */
public class SetNodeExtraRequest extends RequestModel {
    Map<String, String> mExtra;
    private int mNodeId;
    private String mControllerId;

    /**
     * Base constructor.
     * @param extra A map containing extra information for the node.
     * @param nodeId The id of the node associated to the provided extra information.
     * @param controllerId The id of the controller associated to the node.
     */
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
