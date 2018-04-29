package com.example.chaochin.bentleybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    String email;
    String phone;
    String passw;
    String pwConfirm;
    String name;
    boolean registerSuccess;

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

        email = bentleyEmail.getText().toString();
        passw = password.getText().toString();
        pwConfirm = confirmPas.getText().toString();
        phone = phoneNo.getText().toString();
        name = userName.getText().toString();

        if(!email.contains("@bentley.edu")){
            Toast.makeText(this, "Please use bentley email", Toast.LENGTH_SHORT).show();
        }else {
            if(!passw.equals(pwConfirm)){
                Toast.makeText(this, "Please check your password", Toast.LENGTH_SHORT).show();
            }
            else{
                Thread t1 = new Thread(background);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    Log.e("JDBC", "Interrupted Exception");
                }
            }
        }
        if(registerSuccess){
            Toast.makeText(this, "Registration success!", Toast.LENGTH_SHORT).show();
            Intent i1 = new Intent(this,Login.class);
            startActivity(i1);
            finish();
        }
    }

    private Runnable background = new Runnable() {
        public void run(){
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleybooks";
            String username = "CS280";
            String pw = "CS280";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");

            }

            Statement stmt = null;
            Connection con=null;
            try { //create connection and statement objects
                con = DriverManager.getConnection (
                        URL,
                        username,
                        pw);
            } catch (SQLException e) {
                Log.e("JDBC", "problem connecting");
            }

            try {
                con = DriverManager.getConnection(
                        URL,
                        username,
                        pw);
                String query = "insert into user (email, pw, phone, name)"
                        + "values(?, ?, ?, ?)";
                PreparedStatement p = con.prepareStatement(query);

                p.setString(1,email);
                p.setString(2,passw);
                p.setString(3,phone);
                p.setString(4,name);
                p.execute();
                registerSuccess = true;

            } catch (SQLException e) {
                Log.e("JDBC","problems with SQL sent to "+URL+
                        ": "+e.getMessage());
            }

            finally {
                try { //close may throw checked exception
                    if (con != null)
                        con.close();
                } catch(SQLException e) {
                    Log.e("JDBC", "close connection failed");
                }
            };
        }
    };
}
