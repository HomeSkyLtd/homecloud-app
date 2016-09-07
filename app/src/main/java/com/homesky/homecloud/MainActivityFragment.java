package com.homesky.homecloud;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homesky.homecloud.command.Command;
import com.homesky.homecloud.command.GetHouseStateCommand;
import com.homesky.homecloud.command.GetLearntRulesCommand;
import com.homesky.homecloud.command.GetRulesCommand;
import com.homesky.homecloud.command.LoginCommand;
import com.homesky.homecloud.command.LogoutCommand;
import com.homesky.homecloud.command.NewActionCommand;
import com.homesky.homecloud.command.NewAdminCommand;
import com.homesky.homecloud.command.NewRulesCommand;
import com.homesky.homecloud.command.NewUserCommand;
import com.homesky.homecloud.command.RegisterControllerCommand;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.request.NewRulesRequest;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mTokenEditText;
    EditText mControllerIdEditText;
    EditText mNodeIdEditText;
    EditText mValueEditText;
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
    Button mSetNodeExtraButton;
    Button mGetNodesInfoButton;
    Button mAcceptNodeButton;
    Button mRemoveNodeButton;
    TextView mResponseTextView;

    public static MainActivityFragment newInstance(){
        return new MainActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, (token == null) ? "null" : token);

        HomecloudHolder.setUrl("http://192.168.1.37:3000/");
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
        mValueEditText = (EditText)v.findViewById(R.id.value_edit_text);

        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String token = mTokenEditText.getText().toString();
                HomecloudHolder.getInstance().setUsername(username);
                HomecloudHolder.getInstance().setPassword(password);
                HomecloudHolder.getInstance().setToken(token);

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
                int nodeId = 1;
                String controllerId = "1";
                int commandId = 1;
                BigDecimal value = new BigDecimal(4);
                NewActionCommand command = new NewActionCommand(nodeId, controllerId, commandId, value);
                new RequestTask().execute(command);
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
                orStatement.add(new Proposition(">", "1.1", 10));
                orStatement.add(new Proposition("==", "1.1", "2.1"));

                int nodeId = 1;
                int commandId = 2;
                String controllerId = "3";
                BigDecimal value = new BigDecimal(4);
                NewRulesRequest.Rule rule = new NewRulesRequest.Rule(nodeId, controllerId, commandId, value, clause);
                ArrayList<NewRulesRequest.Rule> rules = new ArrayList<>();
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

        mSetNodeExtraButton = (Button)v.findViewById(R.id.set_node_extra_button);
        mSetNodeExtraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
            }
        });

        mGetNodesInfoButton = (Button)v.findViewById(R.id.get_nodes_info_button);
        mGetNodesInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
            }
        });

        mAcceptNodeButton = (Button)v.findViewById(R.id.accept_node_button);
        mAcceptNodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
            }
        });

        mRemoveNodeButton = (Button)v.findViewById(R.id.remove_node_button);
        mRemoveNodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResponseTextView();
            }
        });

        mResponseTextView = (TextView)v.findViewById(R.id.response_text_view);
        return v;
    }

    private void clearResponseTextView(){
        mResponseTextView.setText("");
    }

    private class RequestTask extends AsyncTask<Command, Void, SimpleResponse>{


        @Override
        protected SimpleResponse doInBackground(Command... commands) {
            return commands[0].execute();
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