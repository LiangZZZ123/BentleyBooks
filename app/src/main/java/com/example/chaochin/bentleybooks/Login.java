package com.example.chaochin.bentleybooks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity  {


    private EditText e_email;
    private EditText e_password;
    private Button signIn;
    private Button signUp;
    public String email;
    private String password;
    private boolean loginInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        e_email = (EditText)findViewById(R.id.email);
        e_password = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.sign_in_button);
        signUp = (Button)findViewById(R.id.sign_up_button);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                email = e_email.getText().toString();
                password= e_password.getText().toString();

                if (!email.contains("@bentley.edu")) {
                    Toast.makeText(Login.this, "Please use Bentley email", Toast.LENGTH_LONG).show();
                } else {
                    Thread t1 = new Thread(background);
                    t1.start();

                    if (loginInfo) {
                        Intent intent1 = new Intent(Login.this, Search_ISBN.class);
                        startActivity(intent1);
                    }
                    else{
                        Toast.makeText(Login.this, "No match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
                stmt = con.createStatement();

                ResultSet result = stmt.executeQuery(
                        "SELECT email, pw FROM user;");

                //for each record in City table add City to ArrayList and add city data to log
                while (result.next()) {
                    String emailFromDB = result.getString("email");
                    String pwFromDB = result.getString("pw");
                    if(emailFromDB.equals(email)&&pwFromDB.equals(password)){
                        loginInfo = true;
                    }
                }


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



