package com.example.chaochin.bentleybooks;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.app.NotificationChannel;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity  {

    /*create reference for layout*/
    private EditText e_email;
    private EditText e_password;
    private Button signIn;
    private Button signUp;

    /*to verify if the login info is correct*/
    private boolean loginInfo = false;

    private NotificationManager mNotificationManager;
    private Notification notifyDetails;

    /*Info for Notification*/
    private String contentTitle = "Login Success!";
    private String contentText = "Get back to Application by clicking me";
    private String tickerText = "New Alert, Click Me !!!";

    /*To create UserData object*/
    private String name;
    private String phone;
    public String email;
    private String password;

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

                /*Only Bentley email is allowed*/
                if (!email.contains("@bentley.edu")) {
                    Toast.makeText(Login.this, "Please use Bentley email", Toast.LENGTH_SHORT).show();
                } else {
                    Thread t1 = new Thread(background);
                    t1.start();
                    try {
                        t1.join(); //wait the connection overs
                    } catch (InterruptedException e) {
                        Log.e("JDBC", "Interrupted Exception");
                    }
                }
                loginCheck();
            }
        });

        /*for new user to register*/
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i2 = new Intent(Login.this, SignUp.class);
                startActivity(i2); // go to signUp activity
            }
        });
    }

    /*if user info is correct, login to the mainpage, and set the notification, otherwise
     shows no matched record*/
    public void loginCheck() {
            if (loginInfo) { //if the login information is correct
                Intent intent1 = new Intent(Login.this, Animation.class);
                intent1.putExtra("email",email);
                intent1.putExtra("phone",phone);
                intent1.putExtra("name", name);
                intent1.putExtra("password", password);
                startActivity(intent1);

                /*Start of Notification codes*/
                 mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // The id of the channel.
                String id = "my_channel_01";

                CharSequence names = "hi"; //no meaning
                String description = "yoyo";//no meaning
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(id, name,importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);

                mNotificationManager.createNotificationChannel(mChannel);

                //create intent for action when notification selected
                //from expanded status bar
                Intent notifyIntent = new Intent(this, Search_ISBN.class);
                notifyIntent.putExtra("email",email);
                notifyIntent.putExtra("phone",phone);
                notifyIntent.putExtra("name", name);
                notifyIntent.putExtra("password", password);

                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                
                // Sets an ID for the notification, so it can be updated.
                int notifyID = 1;

                String CHANNEL_ID = "my_channel_01";

                //build notification object and set parameters
                notifyDetails =
                        new Notification.Builder(this)
                                .setContentIntent(pendingIntent)
                                .setContentTitle(contentTitle)   //set Notification text and icon
                                .setContentText(contentText)
                                .setSmallIcon(R.drawable.droid)
                                .setTicker(tickerText)            //set status bar text
                                .setWhen(System.currentTimeMillis())    //timestamp when event occurs
                                .setAutoCancel(true)     //cancel Notification after clicking on it
                                .setChannelId(CHANNEL_ID)
                                .build();
              mNotificationManager.notify(notifyID, notifyDetails);
              /*End of Notification codes*/

                finish();
            } else {
                Toast.makeText(Login.this, "No account records", Toast.LENGTH_SHORT).show();
            }
    }

    /*Thread for the connection*/
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
                        "SELECT * FROM user;");


                while (result.next()) {
                    String emailFromDB = result.getString("email");
                    String pwFromDB = result.getString("pw");

                    /*to find the matched email and password*/
                    if(emailFromDB.equals(email) && pwFromDB.equals(password)){
                        name =  result.getString("name");
                        phone = result.getString("phone");
                        loginInfo = true; //login information is correct
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