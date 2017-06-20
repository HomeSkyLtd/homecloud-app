package com.homesky.homecloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homesky.homecloud.command.AcceptNodeCommand;
import com.homesky.homecloud.command.Command;
import com.homesky.homecloud.command.ForceRuleLearningCommand;
import com.homesky.homecloud.command.GetControllersCommand;
import com.homesky.homecloud.command.GetHouseStateCommand;
import com.homesky.homecloud.command.GetLearntRulesCommand;
import com.homesky.homecloud.command.GetNodesInfoCommand;
import com.homesky.homecloud.command.GetRulesCommand;
import com.homesky.homecloud.command.GetUsersCommand;
import com.homesky.homecloud.command.LoginCommand;
import com.homesky.homecloud.command.LogoutCommand;
import com.homesky.homecloud.command.NewActionCommand;
import com.homesky.homecloud.command.NewAdminCommand;
import com.homesky.homecloud.command.NewRulesCommand;
import com.homesky.homecloud.command.NewUserCommand;
import com.homesky.homecloud.command.RegisterControllerCommand;
import com.homesky.homecloud.command.RemoveNodeCommand;
import com.homesky.homecloud.command.RemoveRuleCommand;
import com.homesky.homecloud.command.SetNodeExtraCommand;
import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.exceptions.NotProperlyInitializedException;
import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.enums.OperatorEnum;
import com.homesky.homecloud_lib.model.notification.Notification;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";

    private Map<String, Integer> mLayoutMappings;
    private int mCurrentFunctionSelection = 0;
    private String mToken;

    EditText mUrlEditText;
    Spinner mSpinner;
    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mTokenEditText;
    EditText mControllerIdEditText;
    EditText mNodeIdEditText;
    EditText mCommandIdEditText;
    EditText mValueEditText;
    EditText mExtraEditText;
    Button mQRButton;
    Button mSendButton;
    TextView mResponseTextView;

    BroadcastReceiver mReceiver;

    public static MainActivityFragment newInstance(){
        return new MainActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = FirebaseInstanceId.getInstance().getToken();

        if(mToken != null)
            HomecloudHolder.setToken(mToken);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Notification s = (Notification)intent.getSerializableExtra(MessageService.NOTIF_MESSAGE);
                Log.d(TAG, "Received notification: " + s.toString());
                mResponseTextView.setText(s.toString());
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mReceiver),
                new IntentFilter(MessageService.NOTIF_RESULT)
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_main, container, false);

        mUrlEditText = (EditText)v.findViewById(R.id.url_edit_text);
        mUsernameEditText = (EditText)v.findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        mTokenEditText = (EditText)v.findViewById(R.id.token_edit_text);
        mControllerIdEditText = (EditText)v.findViewById(R.id.controller_id_edit_text);
        mNodeIdEditText = (EditText)v.findViewById(R.id.node_id_edit_text);
        mCommandIdEditText = (EditText)v.findViewById(R.id.command_id_edit_text);
        mValueEditText = (EditText)v.findViewById(R.id.value_edit_text);
        mExtraEditText = (EditText)v.findViewById(R.id.extra_edit_text);

        mTokenEditText.setText(mToken);

        mResponseTextView = (TextView)v.findViewById(R.id.response_text_view);

        mSpinner = (Spinner)v.findViewById(R.id.function_spinner);
        final String[] functions = getResources().getStringArray(R.array.functions);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, functions);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerArrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateLayout(position, v);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSendButton = (Button)v.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mUrlEditText.getText().toString().equals(""))
                    HomecloudHolder.setUrl("http://" + mUrlEditText.getText().toString());
                else {
                    mResponseTextView.setText("Please, specify the server url");
                    return;
                }

                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String token = mTokenEditText.getText().toString();
                HomecloudHolder.setUsername(username);
                HomecloudHolder.setPassword(password);
                HomecloudHolder.setToken(token);

                int selectedIndex = mSpinner.getSelectedItemPosition();
                String selectedItem = functions[selectedIndex];
                clearResponseTextView();
                switch (selectedItem) {
                    case "Login":
                        login();
                        break;
                    case "Logout":
                        logout();
                        break;
                    case "New admin":
                        newAdmin();
                        break;
                    case "Get house state":
                        getHouseState();
                        break;
                    case "New action":
                        newAction();
                        break;
                    case "Accept node":
                        acceptNode();
                        break;
                    default:
                        mResponseTextView.setText("Invalid function specified");
                }
            }
        });

        mQRButton = (Button)v.findViewById(R.id.camera_button);
        mQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
                }
            }
        });

        mLayoutMappings = getArgumentLayoutMappings();
        updateLayout(0, v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                mControllerIdEditText.setText(contents);
            }
            if(resultCode == getActivity().RESULT_CANCELED){
                //handle cancel
            }
        }
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        super.onStop();
    }

    private void login() {
        LoginCommand command = new LoginCommand();
        new RequestTask().execute(command);
    }

    private void logout() {
        LogoutCommand command = new LogoutCommand();
        new RequestTask().execute(command);
    }

    private void newAdmin() {
        NewAdminCommand command =
                new NewAdminCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
        new RequestTask().execute(command);
    }

    private void newUser() {
        NewUserCommand command =
                new NewUserCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
        new RequestTask().execute(command);
    }

    private void registerController() {
        RegisterControllerCommand command =
                new RegisterControllerCommand(mControllerIdEditText.getText().toString());
        new RequestTask().execute(command);
    }

    private void getHouseState() {
        GetHouseStateCommand command = new GetHouseStateCommand();
        new RequestTask().execute(command);
    }

    private void getRules() {
        GetRulesCommand command = new GetRulesCommand();
        new RequestTask().execute(command);
    }

    private void newAction() {
        boolean isEmpty = (mNodeIdEditText.length()==0) || (mCommandIdEditText.length() == 0);
        if(!isEmpty) {
            int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
            String controllerId = mControllerIdEditText.getText().toString();
            int commandId = Integer.parseInt(mCommandIdEditText.getText().toString());
            BigDecimal value = new BigDecimal(mValueEditText.getText().toString());
            NewActionCommand command = new NewActionCommand(nodeId, controllerId, commandId, value);
            new RequestTask().execute(command);
        }
        else
            mResponseTextView.setText("Node id and command id cannot be empty");
    }

    private void newRules() {
        List<List<Proposition>> clause = new ArrayList<>();
        List<Proposition> orStatement = new ArrayList<Proposition>();
        clause.add(orStatement);
        orStatement.add(new Proposition(OperatorEnum.EQ, "0.1", 0));

        int nodeId = 1;
        int commandId = 0;
        String controllerId = "57d952ec1c059e0e89ec3a9e";
        BigDecimal value = new BigDecimal(0);
        Rule rule = new Rule(nodeId, controllerId, commandId, value, clause);
        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(rule);
        NewRulesCommand command = new NewRulesCommand(rules);
        new RequestTask().execute(command);
    }

    private void removeRules() {
        boolean isEmpty = (mNodeIdEditText.length()==0) || (mCommandIdEditText.length() == 0);
        if(!isEmpty) {
            int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
            String controllerId = mControllerIdEditText.getText().toString();
            int commandId = Integer.parseInt(mCommandIdEditText.getText().toString());
            BigDecimal value = new BigDecimal(mValueEditText.getText().toString());
            RemoveRuleCommand command = new RemoveRuleCommand(
                    new Rule(nodeId, controllerId, commandId, value, null));
            new RequestTask().execute(command);
        }
        else
            mResponseTextView.setText("Node id and command id cannot be empty");
    }

    private void setNodeExtra() {
        boolean isEmpty = (mNodeIdEditText.length()==0);
        if(!isEmpty) {
            int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
            String controllerId = mControllerIdEditText.getText().toString();
            Map<String, String> extra = new HashMap<>();
            String[] extraInput = mExtraEditText.getText().toString().split(":");
            if (extraInput.length == 2) {
                extra.put(extraInput[0], extraInput[1]);
                SetNodeExtraCommand command = new SetNodeExtraCommand(extra, nodeId, controllerId);
                new RequestTask().execute(command);
            }
        }
        else
            mResponseTextView.setText("Node id cannot be empty");
    }

    private void acceptNode() {
        boolean isEmpty = (mNodeIdEditText.length()==0);
        if(!isEmpty) {
            int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
            String controllerId = mControllerIdEditText.getText().toString();
            int accept = 1;
            AcceptNodeCommand command = new AcceptNodeCommand(nodeId, controllerId, accept);
            new RequestTask().execute(command);
        }
        else
            mResponseTextView.setText("Node id cannot be empty");
    }

    private void removeNode() {
        boolean isEmpty = (mNodeIdEditText.length()==0);
        if(!isEmpty) {
            int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
            String controllerId = mControllerIdEditText.getText().toString();
            RemoveNodeCommand command = new RemoveNodeCommand(nodeId, controllerId);
            new RequestTask().execute(command);
        }
        else
            mResponseTextView.setText("Node id cannot be empty");
    }

    private void getNodesInfo() {
        GetNodesInfoCommand command = new GetNodesInfoCommand();
        new RequestTask().execute(command);
    }

    private void getControllers() {
        GetControllersCommand command = new GetControllersCommand();
        new RequestTask().execute(command);
    }

    private void getUsers() {
        GetUsersCommand command = new GetUsersCommand();
        new RequestTask().execute(command);
    }

    private void clearResponseTextView(){
        mResponseTextView.setText("");
    }

    private Map<String, Integer> getArgumentLayoutMappings() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("username", R.id.username_linear_layout);
        mapping.put("password", R.id.password_linear_layout);
        mapping.put("nodeId", R.id.node_id_linear_layout);
        mapping.put("controllerId", R.id.controller_id_linear_layout);
        mapping.put("commandId", R.id.command_id_linear_layout);
        mapping.put("value", R.id.value_linear_layout);
        mapping.put("extra", R.id.extra_linear_layout);
        return mapping;
    }

    private void updateLayout(int selectedIndex, View v) {
        String[] functions = getResources().getStringArray(R.array.functions);

        Map<String, List<String>> functionParams = FunctionParams.functionParams;

        for(String param : functionParams.get(functions[mCurrentFunctionSelection])) {
            LinearLayout ll = (LinearLayout)v.findViewById(mLayoutMappings.get(param));
            ll.setVisibility(View.GONE);
        }
        for(String param : functionParams.get(functions[selectedIndex])) {
            LinearLayout ll = (LinearLayout)v.findViewById(mLayoutMappings.get(param));
            ll.setVisibility(View.VISIBLE);
        }
        mCurrentFunctionSelection = selectedIndex;
    }

    private class RequestTask extends AsyncTask<Command, Void, SimpleResponse>{


        @Override
        protected SimpleResponse doInBackground(Command... commands) {
            try {
                return commands[0].execute();
            }
            catch (NetworkException e) {
                Log.e(TAG, "Received NetworkException", e);
                return SimpleResponse.from("{\"status\": 400, \"errorMessage\": \"" + e.getMessage() + "\"}");
            }
            catch (NotProperlyInitializedException e) {
                Log.e(TAG, "Homecloud library not properly initialized", e);
                return SimpleResponse.from("{\"status\": 400, \"errorMessage\": \"NotProperlyInitializedException\"}");
            }
        }

        @Override
        protected void onPostExecute(SimpleResponse s) {
            super.onPostExecute(s);

            mResponseTextView.setText(s.toString());
            if(s == null)
                mResponseTextView.setText("Invalid response received from server");
            else{
                try {
                    JSONObject obj = new JSONObject(s.toString());
                    mResponseTextView.setText(obj.toString(2));
                }
                catch(JSONException e) {
                    Log.e(TAG, "Error parsing response JSON", e);
                }
            }

        }
    }
}