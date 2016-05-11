package com.example.andumgaming.g370.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.asynctask.LoginAsyncTask;
import com.example.andumgaming.g370.views.asynctask.SignupAsyncTask;

/**
 * Created by ross on 5/5/2016.
 */
public class SignonActivity extends AppCompatActivity {
    private EditText usernameField,passwordField, emailField;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signon_activity);

        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);
        emailField = (EditText)findViewById(R.id.email);

        status = (TextView)findViewById(R.id.textView7);
    }

    public void signonPost (View view) {
        String email = emailField.getText().toString();
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        status = (TextView)findViewById(R.id.textView7);

        new SignupAsyncTask(this,status).execute(email, username,password);
    }
}
