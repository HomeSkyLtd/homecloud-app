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
import com.homesky.homecloud.command.LoginCommand;
import com.homesky.homecloud.command.LogoutCommand;
import com.homesky.homecloud.command.NewAdminCommand;
import com.homesky.homecloud.command.NewUserCommand;
import com.homesky.homecloud.command.RegisterControllerCommand;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import org.json.JSONObject;

public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mTokenEditText;
    EditText mControllerIdEditText;
    Button mLoginButton;
    Button mLogoutButton;
    Button mNewUserButton;
    Button mNewAdminButton;
    Button mRegisterControllerButton;
    TextView mResponseTextView;

    public static MainActivityFragment newInstance(){
        return new MainActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, (token == null) ? "null" : token);

        HomecloudHolder.setUrl("http://192.168.1.35:3000/");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);

        mUsernameEditText = (EditText)v.findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        mTokenEditText = (EditText)v.findViewById(R.id.token_edit_text);
        mControllerIdEditText = (EditText)v.findViewById(R.id.controller_id_edit_text);

        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                LogoutCommand command = new LogoutCommand();
                new RequestTask().execute(command);
            }
        });

        mNewUserButton = (Button)v.findViewById(R.id.new_user_button);
        mNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewUserCommand command =
                        new NewUserCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mNewAdminButton = (Button)v.findViewById(R.id.new_admin_button);
        mNewAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAdminCommand command =
                        new NewAdminCommand(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mRegisterControllerButton = (Button)v.findViewById(R.id.register_controller_button);
        mRegisterControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterControllerCommand command =
                        new RegisterControllerCommand(mControllerIdEditText.getText().toString());
                new RequestTask().execute(command);
            }
        });

        mResponseTextView = (TextView)v.findViewById(R.id.response_text_view);
        return v;
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
            else
                mResponseTextView.setText(s.toString());
        }
    }
}