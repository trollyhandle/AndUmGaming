package com.example.andumgaming.g370.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.asynctask.LoginAsyncTask;

/**
 * Created by ross on 5/5/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameField,passwordField;
    private TextView status,method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);

        status = (TextView)findViewById(R.id.textView7);
    }

    public void login(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        status = (TextView)findViewById(R.id.textView7);

        new LoginAsyncTask(this,status).execute(username,password);
    }
}
