package com.example.andumgaming.g370.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.asynctask.SignupAsyncTask;

/**
 * Created by ross on 5/5/2016.
 */
public class SignupActivity extends AppCompatActivity {
    private EditText usernameField,passwordField;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);

        status = (TextView)findViewById(R.id.textView7);
    }

    public void signupPost(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        status = (TextView)findViewById(R.id.textView7);

        new SignupAsyncTask(this,status).execute(username,password);
    }
}
