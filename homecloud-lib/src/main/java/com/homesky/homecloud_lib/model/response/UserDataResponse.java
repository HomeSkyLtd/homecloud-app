package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDataResponse extends SimpleResponse {
    private static final String TAG = "UserDataRes";
    private List<String> mUsers;

    public UserDataResponse(int status, String errorMessage, List<String> users) {
        super(status, errorMessage);
        mUsers = users;
    }

    public static UserDataResponse from(String jsonStr) {
        if(jsonStr == null) return null;

        try {
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            List<String> users = new ArrayList<>();
            if(status == 200) {
                JSONArray usersJson = obj.getJSONArray(Constants.Fields.GetUsers.USERS);
                for(int i = 0 ; i < usersJson.length() ; ++i){
                    users.add(usersJson.getString(i));
                }
            }
            return new UserDataResponse(status, errorMessage, users);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);

        writer.name(Constants.Fields.GetUsers.USERS);
        writer.beginArray();
        for(String user : mUsers){
            writer.value(user);
        }
        writer.endArray();
    }

    public List<String> getUsers() {
        return mUsers;
    }
}
