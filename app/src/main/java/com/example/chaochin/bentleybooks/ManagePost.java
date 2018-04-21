package com.example.chaochin.bentleybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ManagePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "ManagePost";

    private EditText edit1; //for ISBN input
    private String numberISBN;
    private Spinner spin1;
    private String condition;
    private EditText edit2; //for Price input
    private String price;
    private Thread t = null;

    private ListView listview1;
    private ArrayAdapter aaSpin;
    private ArrayAdapter aaList;
    private String[] conditions = {"Select book condition:", "Excellent", "Good", "Bad", "Broken"};
    private ArrayList<Book> books  = new ArrayList<>();    //store list of (ArrayList)book
    public static final int requestCode_1 = 100;
    public String url;
    public Book a;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageposts);

        edit1 = (EditText)findViewById(R.id.editISBN);
        edit2 = findViewById(R.id.editPrice);


        //set for spinner
        spin1 = (Spinner) findViewById(R.id.spinCondition);
        spin1.setOnItemSelectedListener(this);
        aaSpin = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conditions);
        spin1.setAdapter(aaSpin);

        //set for listView
        listview1 = findViewById(R.id.listBooks);
        listview1.setOnItemClickListener(this);
        listview1.setOnItemLongClickListener(this);
        aaList = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, books);
        listview1.setAdapter(aaList);

    }
    private Runnable background = new Runnable() {
        public void run(){
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleybooks";
            String username = "CS280";
            String password = "CS280";

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
                        password);
            } catch (SQLException e) {
                Log.e("JDBC", "problem connecting");
            }

            try {
                // execute SQL commands to create table, insert data, select contents
                String query = "insert into BOOK2(isbn,bookcondition, price)"
                        + "values(?, ?, ?)";
               PreparedStatement p = con.prepareStatement(query);
               p.setString(1, a.getISBN());
                p.setString(2, a.getCondition());
                p.setString(3, a.getPrice());
                p.execute();

                //clean up
                t = null;


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                ManagePost.this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete this posted book?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                books.remove(i);
                //add code to delete this book from database
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
                a = new Book(numberISBN, condition, price);
                books.add(a);
                t = new Thread(background);
                t.start();

                aaList.notifyDataSetChanged();
            }
            if(resultCode == Activity.RESULT_CANCELED) {
                //BookInformation 回傳cancel後的code
            }
        }
        edit1.setText("");
        spin1.setSelection(0);
        edit2.setText("");
    }


}



