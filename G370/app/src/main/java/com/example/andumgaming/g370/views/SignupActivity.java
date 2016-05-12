package com.example.andumgaming.g370.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.asynctask.LoginAsyncTask;
import com.example.andumgaming.g370.views.asynctask.SignupAsyncTask;
import com.google.gson.Gson;

import Interface.ICallBackListener;

/**
 * Created by ross on 5/5/2016.
 */
public class SignupActivity extends AppCompatActivity {
    private EditText usernameField,passwordField, emailField;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);
        emailField = (EditText)findViewById(R.id.email);

        status = (TextView)findViewById(R.id.textView7);
    }

    private class SignupMsg {
        int success;
        String message;

        public int getSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    };

    public void signupPost (View view) {
        String email = emailField.getText().toString();
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        status = (TextView) findViewById(R.id.textView7);

        ICallBackListener listener = new ICallBackListener() {
            @Override
            public void onCallBack(String result) {
                int duration = Toast.LENGTH_SHORT;
                Gson gson = new Gson();
                SignupMsg signupMsg = gson.fromJson(result, SignupMsg.class);
                Toast.makeText(SignupActivity.this, signupMsg.getMessage(), duration).show();
                if (signupMsg.getSuccess() == 1) {
                    Intent i = new Intent(SignupActivity.this, FullscreenActivity.class);
                    startActivity(i);
                    (SignupActivity.this).finish();
                }
            }
        };

        new SignupAsyncTask(listener).execute(email, username, password);
    }
}
