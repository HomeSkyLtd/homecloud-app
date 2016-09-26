package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.enums.CommandCategoryEnum;
import com.homesky.homecloud_lib.model.enums.DataCategoryEnum;
import com.homesky.homecloud_lib.model.enums.EnumUtil;
import com.homesky.homecloud_lib.model.enums.MeasureStrategyEnum;
import com.homesky.homecloud_lib.model.enums.NodeClassEnum;
import com.homesky.homecloud_lib.model.enums.TypeEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a response from the server including node information.
 */
public class NodesResponse extends SimpleResponse{
    private static final String TAG = "NodesResponse";

    private List<Node> mNodes;

    protected NodesResponse(int status, String errorMessage, List<Node> nodes) {
        super(status, errorMessage);
        mNodes = nodes;
    }

    public List<Node> getNodes() {
        return mNodes;
    }

    /**
     * Creates a {@link NodesResponse} instance from the JSON payload received as response from the server.
     * @param jsonStr The JSON response in string format.
     * @return A {@link NodesResponse} object representing the response.
     */
    public static NodesResponse from(String jsonStr){
        if(jsonStr == null) return null;

        try {
            List<Node> nodes = new ArrayList<>();
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            if(status == 200) {
                JSONArray nodesJSON = obj.getJSONArray(Constants.Fields.NodesResponse.NODES);
                for (int i = 0; i < nodesJSON.length(); ++i) {
                    JSONObject nodeJSON = nodesJSON.getJSONObject(i);
                    int nodeId = nodeJSON.getInt(Constants.Fields.NodesResponse.NODE_ID);
                    String controllerID = nodeJSON.getString(Constants.Fields.NodesResponse.CONTROLLER_ID);
                    long nodeClass = nodeJSON.getLong(Constants.Fields.NodesResponse.NODE_CLASS);
                    int accepted = nodeJSON.getInt(Constants.Fields.NodesResponse.ACCEPTED);
                    int alive = nodeJSON.getInt(Constants.Fields.NodesResponse.ALIVE);

                    Map<String, String> extra = new HashMap<>();
                    JSONObject extraJSON = nodeJSON.getJSONObject(Constants.Fields.NodesResponse.EXTRA);
                    JSONArray extraKeys = extraJSON.names();
                    if(extraKeys != null) {
                        for (int j = 0; j < extraKeys.length(); ++j) {
                            extra.put(extraKeys.getString(j), extraJSON.getString(extraKeys.getString(j)));
                        }
                    }

                    List<DataType> dataType = new ArrayList<>();
                    if(nodeJSON.has(Constants.Fields.NodesResponse.DATA_TYPE)) {
                        JSONArray dataTypeJSON = nodeJSON.getJSONArray(Constants.Fields.NodesResponse.DATA_TYPE);
                        for (int j = 0; j < dataTypeJSON.length(); ++j) {
                            JSONObject unitDataTypeJSON = dataTypeJSON.getJSONObject(j);
                            int id = unitDataTypeJSON.getInt(Constants.Fields.NodesResponse.ID);
                            long measureStrategy = unitDataTypeJSON.getLong(Constants.Fields.NodesResponse.MEASURE_STRATEGY);
                            long type = unitDataTypeJSON.getLong(Constants.Fields.NodesResponse.TYPE);
                            long dataCategory = unitDataTypeJSON.getLong(Constants.Fields.NodesResponse.DATA_CATEGORY);
                            String unit = unitDataTypeJSON.getString(Constants.Fields.NodesResponse.UNIT);

                            JSONArray rangeJSON = unitDataTypeJSON.getJSONArray(Constants.Fields.NodesResponse.RANGE);
                            BigDecimal[] range = {new BigDecimal(rangeJSON.getString(0)), new BigDecimal(rangeJSON.getString(1))};
                            dataType.add(new DataType(
                                    id,
                                    EnumUtil.enumFrom(measureStrategy, MeasureStrategyEnum.class),
                                    EnumUtil.enumFrom(type, TypeEnum.class),
                                    range,
                                    EnumUtil.enumFrom(dataCategory, DataCategoryEnum.class),
                                    unit));
                        }
                    }
                    List<CommandType> commandType = new ArrayList<>();
                    if(nodeJSON.has(Constants.Fields.NodesResponse.COMMAND_TYPE)) {
                        JSONArray commandTypeJSON = nodeJSON.getJSONArray(Constants.Fields.NodesResponse.COMMAND_TYPE);
                        for (int j = 0; j < commandTypeJSON.length(); ++j) {
                            JSONObject unitCommandTypeJSON = commandTypeJSON.getJSONObject(j);
                            int id = unitCommandTypeJSON.getInt(Constants.Fields.NodesResponse.ID);
                            long type = unitCommandTypeJSON.getLong(Constants.Fields.NodesResponse.TYPE);
                            long commandCategory = unitCommandTypeJSON.getLong(Constants.Fields.NodesResponse.COMMAND_CATEGORY);
                            String unit = unitCommandTypeJSON.getString(Constants.Fields.NodesResponse.UNIT);

                            JSONArray rangeJSON = unitCommandTypeJSON.getJSONArray(Constants.Fields.NodesResponse.RANGE);
                            BigDecimal[] range = {new BigDecimal(rangeJSON.getString(0)), new BigDecimal(rangeJSON.getString(1))};
                            commandType.add(new CommandType(
                                    id,
                                    EnumUtil.enumFrom(type, TypeEnum.class),
                                    range,
                                    EnumUtil.enumFrom(commandCategory, CommandCategoryEnum.class),
                                    unit));
                        }
                    }
                    nodes.add(new Node.Builder()
                            .setNodeId(nodeId)
                            .setControllerId(controllerID)
                            .setNodeClass(EnumUtil.setFrom(nodeClass, NodeClassEnum.class))
                            .setAccepted(accepted)
                            .setAlive(alive)
                            .setExtra(extra)
                            .setDataType(dataType)
                            .setCommandType(commandType)
                            .build()
                    );
                }
            }
            return new NodesResponse(status, errorMessage, nodes);
        }
        catch (JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        writer.name(Constants.Fields.NodesResponse.NODES);
        writer.beginArray();
        for(Node n : mNodes){
            writer.beginObject();
            writer.name(Constants.Fields.NodesResponse.NODE_ID).value(n.mNodeId);
            writer.name(Constants.Fields.NodesResponse.CONTROLLER_ID).value(n.mControllerId);
            writer.name(Constants.Fields.NodesResponse.NODE_CLASS).value(EnumUtil.getMultiEnumAsString(n.mNodeClass));
            writer.name(Constants.Fields.NodesResponse.ACCEPTED).value(n.mAccepted);
            writer.name(Constants.Fields.NodesResponse.ALIVE).value(n.mAlive);
            writer.name(Constants.Fields.NodesResponse.EXTRA);
            writer.beginObject();
            for(String key : n.mExtra.keySet()){
                writer.name(key).value(n.mExtra.get(key));
            }
            writer.endObject();
            writer.name(Constants.Fields.NodesResponse.DATA_TYPE);
            writer.beginArray();
            for(DataType dt : n.mDataType){
                writer.beginObject();
                writer.name(Constants.Fields.NodesResponse.ID).value(dt.mId);
                writer.name(Constants.Fields.NodesResponse.MEASURE_STRATEGY).value(dt.mMeasureStrategy.name());
                writer.name(Constants.Fields.NodesResponse.TYPE).value(dt.mType.name());
                writer.name(Constants.Fields.NodesResponse.DATA_CATEGORY).value(dt.mDataCategory.name());
                writer.name(Constants.Fields.NodesResponse.UNIT).value(dt.mUnit);
                writer.name(Constants.Fields.NodesResponse.RANGE);
                writer.beginArray();
                writer.value(dt.mRange[0]);
                writer.value(dt.mRange[1]);
                writer.endArray();
                writer.endObject();
            }
            writer.endArray();

            writer.name(Constants.Fields.NodesResponse.COMMAND_TYPE);
            writer.beginArray();
            for(CommandType ct : n.mCommandType){
                writer.beginObject();
                writer.name(Constants.Fields.NodesResponse.ID).value(ct.mId);
                writer.name(Constants.Fields.NodesResponse.TYPE).value(ct.mType.name());
                writer.name(Constants.Fields.NodesResponse.COMMAND_CATEGORY).value(ct.mCommandCategory.name());
                writer.name(Constants.Fields.NodesResponse.UNIT).value(ct.mUnit);
                writer.name(Constants.Fields.NodesResponse.RANGE);
                writer.beginArray();
                writer.value(ct.mRange[0]);
                writer.value(ct.mRange[1]);
                writer.endArray();
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        }
        writer.endArray();
    }

    /**
     * Represents a node (sensor or actuator) in the house.
     */
    public static class Node{
        int mNodeId, mAccepted, mAlive;
        String mControllerId;
        EnumSet<NodeClassEnum> mNodeClass;
        Map<String, String> mExtra;
        List<DataType> mDataType;
        List<CommandType> mCommandType;

        private Node(int nodeId, String controllerId, EnumSet<NodeClassEnum> nodeClass, int accepted, int alive,
                    Map<String, String> extra, List<DataType> dataType, List<CommandType> commandType) {
            mNodeId = nodeId;
            mControllerId = controllerId;
            mNodeClass = nodeClass;
            mAccepted = accepted;
            mAlive = alive;
            mExtra = extra;
            mDataType = dataType;
            mCommandType = commandType;
        }

        public int getNodeId() {
            return mNodeId;
        }

        public int getAccepted() {
            return mAccepted;
        }

        public int getAlive() {
            return mAlive;
        }

        public String getControllerId() {
            return mControllerId;
        }

        public EnumSet<NodeClassEnum> getNodeClass() {
            return mNodeClass;
        }

        public Map<String, String> getExtra() {
            return mExtra;
        }

        public List<DataType> getDataType() {
            return mDataType;
        }

        public List<CommandType> getCommandType() {
            return mCommandType;
        }

        public static class Builder{
            int mNodeId;
            String mControllerId = "";
            EnumSet<NodeClassEnum> mNodeClass;
            int mAccepted, mAlive;
            Map<String, String> mExtra = null;
            List<DataType> mDataType = null;
            List<CommandType> mCommandType = null;

            public Node build(){
                return new Node(mNodeId, mControllerId, mNodeClass, mAccepted, mAlive, mExtra, mDataType, mCommandType);
            }

            public Builder setNodeId(int nodeId) {
                mNodeId = nodeId;
                return this;
            }

            public Builder setControllerId(String controllerId) {
                mControllerId = controllerId;
                return this;
            }

            public Builder setNodeClass(EnumSet<NodeClassEnum> nodeClass) {
                mNodeClass = nodeClass;
                return this;
            }

            public Builder setAccepted(int accepted) {
                mAccepted = accepted;
                return this;
            }

            public Builder setAlive(int alive) {
                mAlive = alive;
                return this;
            }

            public Builder setExtra(Map<String, String> extra) {
                mExtra = extra;
                return this;
            }

            public Builder setDataType(List<DataType> dataType) {
                mDataType = dataType;
                return this;
            }

            public Builder setCommandType(List<CommandType> commandType) {
                mCommandType = commandType;
                return this;
            }

        }
    }

    /**
     * Represents a data type in a node declaration.
     */
    public static class DataType{
        int  mId;
        MeasureStrategyEnum mMeasureStrategy;
        TypeEnum mType;
        DataCategoryEnum mDataCategory;
        String mUnit;
        BigDecimal[] mRange;

        DataType(int id, MeasureStrategyEnum measureStrategy, TypeEnum type, BigDecimal[] range,
                 DataCategoryEnum dataCategory, String unit) {
            mId = id;
            mMeasureStrategy = measureStrategy;
            mType = type;
            mRange = range;
            mDataCategory = dataCategory;
            mUnit = unit;
        }

        /**
         * Gets the data id.
         * @return The data id.
         */
        public int getId() {
            return mId;
        }

        /**
         * Gets the measure strategy (periodic or event-based).
         * @return The measure strategy.
         */
        public MeasureStrategyEnum getMeasureStrategy() {
            return mMeasureStrategy;
        }

        /**
         * Gets the data type (integer, decimal, etc.).
         * @return The data type.
         */
        public TypeEnum getType() {
            return mType;
        }

        /**
         * Gets the data category (temperature, humidity, etc.).
         * @return The data category.
         */
        public DataCategoryEnum getDataCategory() {
            return mDataCategory;
        }

        /**
         * Gets the data unit.
         * @return The data unit.
         */
        public String getUnit() {
            return mUnit;
        }

        /**
         * Gets the data range.
         * @return An array in the form {min, max}.
         */
        public BigDecimal[] getRange() {
            return mRange;
        }
    }

    /**
     * Represents a command type in a node declaration.
     */
    public static class CommandType{
        int mId;
        TypeEnum mType;
        CommandCategoryEnum mCommandCategory;
        String mUnit;
        BigDecimal[] mRange;

        CommandType(int id, TypeEnum type, BigDecimal[] range, CommandCategoryEnum commandCategory, String unit) {
            mId = id;
            mType = type;
            mRange = range;
            mCommandCategory = commandCategory;
            mUnit = unit;
        }

        /**
         * Gets the command id.
         * @return The command id.
         */
        public int getId() {
            return mId;
        }

        /**
         * Gets the command type (integer, decimal, etc.).
         * @return The command type.
         */
        public TypeEnum getType() {
            return mType;
        }

        /**
         * Gets the command category (on/off, temperature, etc.).
         * @return The command category.
         */
        public CommandCategoryEnum getCommandCategory() {
            return mCommandCategory;
        }

        /**
         * Gets the command unit.
         * @return The command unit.
         */
        public String getUnit() {
            return mUnit;
        }

        /**
         * Gets the range of values accepted by the command.
         * @return An array in the form {min, max}.
         */
        public BigDecimal[] getRange() {
            return mRange;
        }
    }
}
