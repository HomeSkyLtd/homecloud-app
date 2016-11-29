package com.homesky.homecloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.enums.OperatorEnum;
import com.homesky.homecloud_lib.model.notification.Notification;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mTokenEditText;
    EditText mControllerIdEditText;
    EditText mNodeIdEditText;
    EditText mCommandIdEditText;
    EditText mValueEditText;
    EditText mExtraEditText;
    Button mQRButton;
    Button mLoginButton;
    Button mLogoutButton;
    Button mNewUserButton;
    Button mNewAdminButton;
    Button mRegisterControllerButton;
    Button mGetHouseStateButton;
    Button mNewActionButton;
    Button mGetRulesButton;
    Button mNewRulesButton;
    Button mGetLearntRulesButton;
    Button mRemoveRuleButton;
    Button mSetNodeExtraButton;
    Button mGetNodesInfoButton;
    Button mAcceptNodeButton;
    Button mRemoveNodeButton;
    Button mGetControllersButton;
    Button mGetUsersButton;
    Button mForceLearningButton;
    TextView mResponseTextView;

    BroadcastReceiver mReceiver;

    public static MainActivityFragment newInstance(){
        return new MainActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, (token == null) ? "null" : token);

        HomecloudHolder.setUrl("http://ec2-52-67-3-31.sa-east-1.compute.amazonaws.com:3000/");
//        HomecloudHolder.setUrl("http://192.168.1.37:3000/");

        if(token != null)
            HomecloudHolder.setToken(token);

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
        View v = inflater.inflate(R.layout.activity_main, container, false);

        mUsernameEditText = (EditText)v.findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        mTokenEditText = (EditText)v.findViewById(R.id.token_edit_text);
        mControllerIdEditText = (EditText)v.findViewById(R.id.controller_id_edit_text);
        mNodeIdEditText = (EditText)v.findViewById(R.id.node_id_edit_text);
        mCommandIdEditText = (EditText)v.findViewById(R.id.command_id_edit_text);
        mValueEditText = (EditText)v.findViewById(R.id.value_edit_text);
        mExtraEditText = (EditText)v.findViewById(R.id.extra_edit_text);

        mTokenEditText.setText(HomecloudHolder.getInstance().getToken());

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

        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String token = mTokenEditText.getText().toString();
                HomecloudHolder.setUsername(username);
                HomecloudHolder.setPassword(password);
                HomecloudHolder.setToken(token);

                LoginCommand command = new LoginCommand();
                new RequestTask().execute(command);

            }
        });

        mLogoutButton = (Button)v.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                LogoutCommand command = new LogoutCommand();
                new RequestTask().execute(command);
            }
        });

        mNewUserButton = (Button)v.findViewById(R.id.new_user_button);
        mNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                NewUserCommand command =
                        new NewUserCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mNewAdminButton = (Button)v.findViewById(R.id.new_admin_button);
        mNewAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                NewAdminCommand command =
                        new NewAdminCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mRegisterControllerButton = (Button)v.findViewById(R.id.register_controller_button);
        mRegisterControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                RegisterControllerCommand command =
                        new RegisterControllerCommand(mControllerIdEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mGetHouseStateButton = (Button)v.findViewById(R.id.get_house_state_button);
        mGetHouseStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetHouseStateCommand command = new GetHouseStateCommand();
                new RequestTask().execute(command);
            }
        });

        mNewActionButton = (Button)v.findViewById(R.id.new_action_button);
        mNewActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                boolean isEmpty = (mNodeIdEditText.length()==0) || (mCommandIdEditText.length() == 0);
                if(!isEmpty) {
                    int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
                    String controllerId = mControllerIdEditText.getText().toString();
                    int commandId = Integer.parseInt(mCommandIdEditText.getText().toString());
                    BigDecimal value = new BigDecimal(mValueEditText.getText().toString());
                    NewActionCommand command = new NewActionCommand(nodeId, controllerId, commandId, value);
                    new RequestTask().execute(command);
                }
            }
        });

        mGetRulesButton = (Button)v.findViewById(R.id.get_rules_button);
        mGetRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetRulesCommand command = new GetRulesCommand();
                new RequestTask().execute(command);
            }
        });

        mNewRulesButton = (Button)v.findViewById(R.id.new_rules_button);
        mNewRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
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
        });

        mGetLearntRulesButton = (Button)v.findViewById(R.id.get_learnt_rules_button);
        mGetLearntRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetLearntRulesCommand command = new GetLearntRulesCommand();
                new RequestTask().execute(command);
            }
        });

        mRemoveRuleButton = (Button)v.findViewById(R.id.remove_rule_button);
        mRemoveRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
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
            }
        });

        mSetNodeExtraButton = (Button)v.findViewById(R.id.set_node_extra_button);
        mSetNodeExtraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
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
            }
        });

        mGetNodesInfoButton = (Button)v.findViewById(R.id.get_nodes_info_button);
        mGetNodesInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetNodesInfoCommand command = new GetNodesInfoCommand();
                new RequestTask().execute(command);
            }
        });

        mAcceptNodeButton = (Button)v.findViewById(R.id.accept_node_button);
        mAcceptNodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                boolean isEmpty = (mNodeIdEditText.length()==0);
                if(!isEmpty) {
                    int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
                    String controllerId = mControllerIdEditText.getText().toString();
                    int accept = 1;
                    AcceptNodeCommand command = new AcceptNodeCommand(nodeId, controllerId, accept);
                    new RequestTask().execute(command);
                }
            }
        });

        mRemoveNodeButton = (Button)v.findViewById(R.id.remove_node_button);
        mRemoveNodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                boolean isEmpty = (mNodeIdEditText.length()==0);
                if(!isEmpty) {
                    int nodeId = Integer.parseInt(mNodeIdEditText.getText().toString());
                    String controllerId = mControllerIdEditText.getText().toString();
                    RemoveNodeCommand command = new RemoveNodeCommand(nodeId, controllerId);
                    new RequestTask().execute(command);
                }
            }
        });

        mGetControllersButton = (Button)v.findViewById(R.id.get_controllers_button);
        mGetControllersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetControllersCommand command = new GetControllersCommand();
                new RequestTask().execute(command);
            }
        });

        mGetUsersButton = (Button)v.findViewById(R.id.get_users_button);
        mGetUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                GetUsersCommand command = new GetUsersCommand();
                new RequestTask().execute(command);
            }
        });

        mForceLearningButton = (Button)v.findViewById(R.id.force_learning_button);
        mForceLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                ForceRuleLearningCommand command = new ForceRuleLearningCommand();
                new RequestTask().execute(command);
            }
        });

        mResponseTextView = (TextView)v.findViewById(R.id.response_text_view);
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

    private void clearResponseTextView(){
        mResponseTextView.setText("");
    }

    private class RequestTask extends AsyncTask<Command, Void, SimpleResponse>{


        @Override
        protected SimpleResponse doInBackground(Command... commands) {
            try {
                return commands[0].execute();
            }
            catch (NetworkException e) {
                Log.e(TAG, "Received NetworkException", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(SimpleResponse s) {
            super.onPostExecute(s);

            if(s == null)
                mResponseTextView.setText("Error making request");
            else{
                mResponseTextView.setText(s.toString());
            }

        }
    }
}