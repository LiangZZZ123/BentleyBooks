package com.example.chaochin.bentleybooks;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 4/11/2018.
 */

public class Search_ISBN extends AppCompatActivity{
    private EditText edit_search;
    private TextView user_id;
    private Button go;
    private ListView listview;
    private ArrayList<String> result = new ArrayList<String>();
    int row=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        //give reference
       edit_search= (EditText) findViewById(R.id.search_edit);
       listview = (ListView) findViewById(R.id.list);
       user_id=(TextView) findViewById(R.id.username_view);
       go=(Button) findViewById(R.id.search_button);


    }
}
