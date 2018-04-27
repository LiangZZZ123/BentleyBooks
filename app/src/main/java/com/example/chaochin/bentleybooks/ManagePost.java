package com.example.chaochin.bentleybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManagePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, TextToSpeech.OnInitListener {
    private static final String tag = "ManagePost";

    private TextView viewName;
    private EditText edit1; //for ISBN input
    private String numberISBN;
    private Spinner spin1;
    private String condition;
    private EditText edit2; //for Price input
    private String price;
    private Thread t1 = null;
    private String bookid;
    private TextToSpeech speaker;

    private ListView listview1;
    private ArrayAdapter aaSpin;
    private ArrayAdapter aaList;
    private String[] conditions = {"Select book condition:", "Excellent", "Good", "Bad", "Broken"};
    private ArrayList<Book> books  = new ArrayList<>();    //store list of (ArrayList)book
    public static final int requestCode_1 = 100;
    public String url;
    public Book book;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageposts);

        viewName = findViewById(R.id.viewUsername);
        viewName.setText(Search_ISBN.user.getName());

        edit1 = findViewById(R.id.editISBN);
        edit2 = findViewById(R.id.editPrice);


        //set for spinner
        spin1 = findViewById(R.id.spinCondition);
        spin1.setOnItemSelectedListener(this);
        aaSpin = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conditions);
        spin1.setAdapter(aaSpin);

        //set for listView
        listview1 = findViewById(R.id.listBooks);
        listview1.setOnItemClickListener(this);
        listview1.setOnItemLongClickListener(this);
        aaList = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, books);
        listview1.setAdapter(aaList);

        t1 = new Thread((backgroundLoad));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        speaker = new TextToSpeech(this, this);

    }




    //create option menu and link it to menu(menu_manageposts) created in xml
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manageposts, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //respond to item selected in option menu
    public boolean onOptionsItemSelected(MenuItem item) {
        numberISBN = String.valueOf(edit1.getText().toString());
        price = String.valueOf(edit2.getText().toString());

        switch (item.getItemId()) {
            case R.id.add:
                //For convenience, delete condition "(numberISBN.length() == 13 || numberISBN.length() == 10) && " in test
                if (!price.equals("") && !condition.equals("Select book condition:")){
                    //ArrayList book  = new ArrayList<>(); //store ISBN and BookCondition and Price for one book
                    url = "http://www.bookfinder4u.com/IsbnSearch.aspx?isbn=" + numberISBN;

                    Intent intent1 = new Intent(this, BookInformation.class);
                    intent1.putExtra("webView", url);
                    startActivityForResult(intent1, requestCode_1);
                }else
                    Toast.makeText(ManagePost.this, "Please check your input", Toast.LENGTH_LONG).show();
                return true;

            case R.id.returnPage:
                Intent intentMainPage = new Intent(this, Search_ISBN.class);
                startActivity(intentMainPage);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Spinner manipulation
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (conditions[i]) {
            case "Excellent":
                condition = "Excellent condition";
                break;
            case "Good":
                condition = "Good condition";
                break;
            case "Bad":
                condition = "Bad condition";
                break;
            case "Broken":
                condition = "Broken condition";
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }


    //listener method for listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                ManagePost.this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete this posted book?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookid = books.get(i).getBookid();
                Log.e("JDBC", "Start delete: bookid" + bookid);
                books.remove(i);
                //code to delete this book from database
                t1 = new Thread(backgroundDelete);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                aaList.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.show();
        return true;    //return true shows that the event is consumed, no other click events(on ItemClick) will be notified.
    }


    //Receive inofmation from (activity)BookInformation
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode_1) {
            if (resultCode == Activity.RESULT_OK) {
                //BookInformation 回傳confirmPost後的code
                bookid = new SimpleDateFormat("yyMMddhhmmssMs").format(new Date());
                book = new Book(bookid, numberISBN, condition, price, Search_ISBN.user.getName(), Search_ISBN.user.getEmail());
                Toast.makeText(ManagePost.this, "I'm" + Search_ISBN.user.getName(), Toast.LENGTH_LONG).show();
                books.add(book);
                t1 = new Thread(backgroundAdd);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //when we've added a book into database, do we need to load the database to local again???    what if add to database not successful?
                books.clear();
                t1 = new Thread(backgroundLoad);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                aaList.notifyDataSetChanged();

                if (speaker.isSpeaking()) {
                    Log.i(tag, "Speaker Speaking");
                    speaker.stop();
                    // else start speech
                } else {
                    Log.i(tag, "Speaker Not Already Speaking");
                    speak( "You have successfully posted a book");
                }
            }
            if(resultCode == Activity.RESULT_CANCELED) {
                //BookInformation 回傳cancel後的code
            }
        }
        edit1.setText("");
        spin1.setSelection(0);
        edit2.setText("");
    }


    //set up syntax for load books from database
    private Runnable backgroundLoad = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleybooks";
            String username = "CS280";
            String password = "CS280";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");
            }

            Connection con = null;
            Statement text = null;
            try {
                con = DriverManager.getConnection(URL, username, password);

                text = con.createStatement();
                ResultSet result = text.executeQuery("select * from book");

                while ( (result.next())){
                    bookid = result.getString("bookid2");
                    numberISBN = result.getString("isbn");
                    condition = result.getString("bookcondition");
                    price = result.getString("price");
                    book = new Book(bookid, numberISBN, condition, price, Search_ISBN.user.getName(), Search_ISBN.user.getEmail());
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //set up syntax for add book into database
    private Runnable backgroundAdd = new Runnable() {
        public void run(){
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleybooks";
            String username = "CS280";
            String password = "CS280";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");
            }

            Connection con = null;
            Statement text = null;
            try { //create connection and statement objects
                con = DriverManager.getConnection (URL, username, password);
            } catch (SQLException e) {
                Log.e("JDBC", "problem connecting");
            }

            try {
                // execute SQL commands to create table, insert data, select contents
                String query = "insert into book (bookid2, isbn,bookcondition, price, seller, email)"
                        + "values(?, ?, ?, ?, ?, ?)";
                PreparedStatement p = con.prepareStatement(query);

                p.setString(1, book.getBookid());
                p.setString(2, book.getISBN());
                p.setString(3, book.getCondition());
                p.setString(4, book.getPrice());
                p.setString(5, Search_ISBN.user.getName());
                p.setString(6, Search_ISBN.user.getEmail());
                p.execute();
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

    //set up syntax for delete book into database
    private Runnable backgroundDelete = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleybooks";
            String username = "CS280";
            String password = "CS280";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");
            }

            Connection con = null;
            Statement text = null;
            try {
                con = DriverManager.getConnection(URL, username, password);

                con.createStatement().execute("delete from book where bookid2 = '"+ bookid +"'");
//                text.execute("delete from book where bookid = '7'");
                Log.e("JDBC", "delete succeed");
                } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    //    ------------------------------------------------TextToSpeechPart_Begin------------------------------------------------------------------
    public void speak(String output) {
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);
            //int result = speaker.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                // speak("Please enter your tasks");
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    // on destroy
    public void onDestroy() {

        // shut down TTS engine
        if (speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }
//    ------------------------------------------------TextToSpeechPartEnd------------------------------------------------------------------

}



