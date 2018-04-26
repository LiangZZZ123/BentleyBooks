package com.example.chaochin.bentleybooks;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Search_ISBN extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private TextView viewEmail;
    public static String email;
    private EditText editisbn;
    private String isbn;
    private TextView username;
    private Button go;
    private ListView listbook;
    private ArrayAdapter aaList;
    private ArrayList<Book> booksShow = new ArrayList<>();
    private Thread t1 = null;


    //for test use, this should be replaced by database
//    Book book1 = new Book("111", "good", "11");
//    Book book2 = new Book("222", "bad", "12");
//    Book book3 = new Book("333", "terrible", "13");
//    Book book4 = new Book("111", "excellent", "11");
//    public Book[] booksOnline = {book1, book2, book3, book4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

//        ???
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(false);

        //give reference
        viewEmail = findViewById(R.id.viewUsername);
        username = (TextView) findViewById(R.id.viewUsername);
        editisbn = (EditText) findViewById(R.id.search_edit);
        go = (Button) findViewById(R.id.search_button);
        listbook = (ListView) findViewById(R.id.list);

        //receive userEmail and show it in interface
        Intent intent = getIntent();
        email = intent.getStringExtra("emailIntent");
        viewEmail.setText(email);

        go.setOnClickListener(this);

        listbook.setOnItemClickListener(this);
        listbook.setOnItemLongClickListener(this);
        aaList = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, booksShow);
        listbook.setAdapter(aaList);

    }

    //create option menu and link it to menu(menu_mainpage) created in xml
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainpage, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //respond to item selected in option menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.managepost:
                booksShow.clear();
                aaList.notifyDataSetChanged();
                Intent intentManagePost = new Intent(this, ManagePost.class);
                startActivity(intentManagePost);
                return true;

            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    //listener method for (Button)go
    @Override
    public void onClick(View view) {
        isbn = editisbn.getText().toString();
        t1 = new Thread((backgroundLoad));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        aaList.notifyDataSetChanged();
    }

    //listener method for listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                Search_ISBN.this);
        alert.setTitle("Confirm Page:");
        alert.setMessage("Are you sure to buy this book? This will lead you to email page. " +
                "\nIf your email app has already started, make sure to exit kill email in background first and do this again");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //intent for sending email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zhangliangjustin@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n\nI want to buy your posted book: <<"
                        + booksShow.get(i) + ">>.\nCan we meet at Lacava tomorrow 6 pm?\nThanks.\n\n\nZhang Liang");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request for book");
                startActivity(emailIntent);
                //For refine: add code to show this book is required by once
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
        return true;
    }

    //method to search book based on ISBN
//    public void selectISBN(String isbn) {
//        booksShow.clear();
////        isbn = editisbn.getText().toString();
//        for (Book book : booksOnline) {
//            if (isbn.equals(book.getISBN()))
//                booksShow.add(book);
//        }
//    }




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
//                executeQuery() Vs executeUpdate() Vs execute() see difference
                ResultSet result = text.executeQuery("select * from book where isbn = '"+isbn +"'");

                while ( (result.next())){
                    String bookid = result.getString("bookid2");
                    String numberISBN = result.getString("isbn");
                    String condition = result.getString("bookcondition");
                    String price = result.getString("price");
                    Book book = new Book(bookid, numberISBN, condition, price);
                    booksShow.add(book);
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

}


