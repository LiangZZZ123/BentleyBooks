package com.example.chaochin.bentleybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManagePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private static final String TAG = "ManagePost";

    private EditText edit1; //for ISBN input
    private String numberISBN;
    private Spinner spin1;
    private String condition;
    private EditText edit2; //for Price input
    private String price;

    private ListView listview1;
    private ArrayAdapter aaSpin;
    private ArrayAdapter aaList;
    private String[] conditions = {"Select book condition:", "Excellent", "Good", "Bad", "Broken"};
    private ArrayList<ArrayList<String>> books  = new ArrayList<>();    //store list of (ArrayList)book
    public static final int requestCode_1 = 100;
    public String url;



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
        aaList = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, books);
        listview1.setAdapter(aaList);
    }

    //Spinner manipulation
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (conditions[i]) {
            case "Excellent":
                condition = "Excellent condition,";
                break;
            case "Good":
                condition = "Good condition,";
                break;
            case "Bad":
                condition = "Bad condition,";
                break;
            case "Broken":
                condition = "Broken condition,";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                if (numberISBN.length() == 13 && !price.equals("") && !condition.equals("Select book condition:")){
                    ArrayList book  = new ArrayList<>(); //store ISBN and BookCondition and Price for one book
                    url = "https://tw.yahoo.com/";

                    Intent intent1 = new Intent(this, BookInformation.class);
                    intent1.putExtra("webView", url);
                    startActivityForResult(intent1, requestCode_1);

                    //Liang: 下面add的code應該要放在onActivityResult的方法下，我把方法加在下面了
                    book.add(numberISBN);
                    book.add(condition);
                    book.add(price);
                    books.add(book);
                    aaList.notifyDataSetChanged();;

                    edit1.setText("");
                    spin1.setSelection(0);
                    edit2.setText("");
                }else
                    Toast.makeText(ManagePost.this, "Please check your input", Toast.LENGTH_LONG).show();
                return true;


//            case R.id.delete:
//
//                return true;

            case R.id.returnPage:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listener method for listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode_1) {
            if (resultCode == Activity.RESULT_OK) {
                //BookInformation 回傳confirmPost後的code
            }
            if(resultCode == Activity.RESULT_CANCELED) {
                //BookInformation 回傳cancel後的code
            }
        }
    }

            //write "delete" with context menu and AlertDialog
//    this.getListView().setLongClickable(true);
// this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
//            //Do your tasks here
//
//
//            AlertDialog.Builder alert = new AlertDialog.Builder(
//                    Activity.this);
//            alert.setTitle("Alert!!");
//            alert.setMessage("Are you sure to delete record");
//            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //do your work here
//                    dialog.dismiss();
//
//                }
//            });
//            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.dismiss();
//                }
//            });
//
//            alert.show();
//
//            return true;
//        }
//    });

}



