package com.example.chaochin.bentleybooks;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 4/11/2018.
 */

public class Search_ISBN extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private EditText editisbn;
    private String isbn;
    private TextView username;
    private Button go;
    private ListView listbook;
    private ArrayAdapter aaList;
    private ArrayList<Book> booksShow = new ArrayList<>();


    //for test use, this should be replaced by database
    Book book1 = new Book("111", "good", "11");
    Book book2 = new Book("222", "bad", "12");
    Book book3 = new Book("333", "terrible", "13");
    Book book4 = new Book("111", "excellent", "11");
    private Book[] booksOnline = {book1, book2, book3, book4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

//        ???
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(false);

        //give reference
        username = (TextView) findViewById(R.id.username_view);
        editisbn = (EditText) findViewById(R.id.search_edit);
        go = (Button) findViewById(R.id.search_button);
        listbook = (ListView) findViewById(R.id.list);

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
        selectISBN(editisbn.getText().toString());
        aaList.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                Search_ISBN.this);
        alert.setTitle("Confirm Page:");
        alert.setMessage("Are you sure to buy this book? This will lead you to email page. " +
                "\nIf your email doesn't start, make sure to to exit email first and do this again");
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

    public void selectISBN(String isbn) {
        booksShow.clear();
//        isbn = editisbn.getText().toString();
        for (Book book : booksOnline) {
            if (isbn.equals(book.getISBN()))
                booksShow.add(book);
        }
    }

}


