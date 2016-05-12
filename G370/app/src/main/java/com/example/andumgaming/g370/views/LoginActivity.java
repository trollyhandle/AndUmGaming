package com.example.andumgaming.g370.views;

import android.app.Activity;
import android.content.Context;
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
public class LoginActivity extends AppCompatActivity {
    private EditText usernameField,passwordField;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);

        status = (TextView)findViewById(R.id.textView7);
    }


    private class LoginMsg {
        int success;
        String message;

        public int getSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public void loginPost(View view){
        ICallBackListener listener = new ICallBackListener() {
            @Override
            public void onCallBack(String result) {
                System.out.println("LOGIN callback: " + result);
                int duration = Toast.LENGTH_SHORT;
                Gson gson = new Gson();
                LoginMsg loginMsg = gson.fromJson(result, LoginMsg.class);
                Toast.makeText(LoginActivity.this, loginMsg.getMessage(), duration).show();
                if (loginMsg.getSuccess() == 1) {
                    Intent i = new Intent(LoginActivity.this, FullscreenActivity.class);
                    startActivity(i);
                    (LoginActivity.this).finish();
                }
            }
        };

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        LoginAsyncTask task = new LoginAsyncTask(listener);
        task.execute(username, password);
    }

    public void signupPost (View view) {

        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
        (LoginActivity.this).finish();
    }


}
