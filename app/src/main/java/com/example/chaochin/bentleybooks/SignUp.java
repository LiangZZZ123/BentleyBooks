package com.example.chaochin.bentleybooks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Melih on 4/7/2018.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextView bentleyEmailTxt;
    private TextView usernameTxt;
    private TextView passwordTxt;
    private TextView confirmPasTxt;
    private TextView phoneNoTxt;

    private EditText bentleyEmail;
    private EditText userName;
    private EditText password;
    private EditText confirmPas;
    private EditText phoneNo;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        bentleyEmailTxt=(TextView) findViewById(R.id.bentleyEmailTxt);
        usernameTxt=(TextView) findViewById(R.id.usernameTxt);
        passwordTxt=(TextView) findViewById(R.id.passwordTxt);
        confirmPasTxt=(TextView) findViewById(R.id.confirmPasTxt);
        phoneNoTxt=(TextView) findViewById(R.id.phoneNoTxt);

        bentleyEmail=(EditText) findViewById(R.id.bentleyEmail);
        userName=(EditText) findViewById(R.id.userName);
        password=(EditText) findViewById(R.id.password);
        confirmPas=(EditText) findViewById(R.id.confirmPas);
        phoneNo=(EditText) findViewById(R.id.phoneNo);

        registerButton=(Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
    }
    public void onClick(View v) {

    }
}
