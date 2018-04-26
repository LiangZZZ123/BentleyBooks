package com.example.chaochin.bentleybooks;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private boolean searchComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        e_email = (EditText)findViewById(R.id.email);
        e_password = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.sign_in_button);
        signUp = (Button)findViewById(R.id.sign_up_button);
        e_email.setHint("Please enter your Bentley email");
        e_password.setHint("Password");


        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                email = e_email.getText().toString();
                password= e_password.getText().toString();

                if (!email.contains("@bentley.edu")) {
                    Toast.makeText(Login.this, "Please use Bentley email", Toast.LENGTH_LONG).show();
                } else {
                    Thread t1 = new Thread(background);
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        Log.e("JDBC", "Interrupted Exception");
                    }
                }
                loginCheck();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i2 = new Intent(Login.this, SignUp.class);
                startActivity(i2);
            }
        });
    }

    public void loginCheck() {
            if (loginInfo) {
                Intent intent1 = new Intent(Login.this, Search_ISBN.class);
                startActivity(intent1);
                finish();
            } else {
                Toast.makeText(Login.this, "No account records", Toast.LENGTH_LONG).show();
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
                stmt = con.createStatement();

                ResultSet result = stmt.executeQuery(
                        "SELECT email, pw FROM user;");


                while (result.next()) {
                    String emailFromDB = result.getString("email");
                    String pwFromDB = result.getString("pw");
                    if(emailFromDB.equals(email) && pwFromDB.equals(password)){
                        loginInfo = true;
                        break;
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