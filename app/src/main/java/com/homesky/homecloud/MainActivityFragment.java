package com.homesky.homecloud;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homesky.homecloud.command.Command;
import com.homesky.homecloud.command.LoginCommand;

import org.json.JSONObject;

public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";

    Button mLoginButton;
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
        HomecloudHolder.setUsername("admin1");
        HomecloudHolder.setPassword("mypass");
        HomecloudHolder.setToken("12345");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login params
                LoginCommand command = new LoginCommand();
                new RequestTask().execute(command);
            }
        });

        mResponseTextView = (TextView)v.findViewById(R.id.response_text_view);
        return v;
    }

    private class RequestTask extends AsyncTask<Command, Void, String>{


        @Override
        protected String doInBackground(Command... commands) {
            return commands[0].execute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null)
                mResponseTextView.setText("Error making request");
            else
                mResponseTextView.setText(s);
        }
    }
}