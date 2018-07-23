package com.example.akanksha.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {


    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    Button b;
    Bundle bundle;
    public static final int DETAILS_REQUEST_CODE = 22;
    public static final int DETAILS_RESULT_CODE = 1012;
    public static final int DETAILS_RESULT_NOT_CHANGE_CODE = 1018;


    String title;
    String desc;
    String date;
    String time;
    String category;
    int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDesc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv1=findViewById(R.id.title);
        tv2=findViewById(R.id.desc);
        tv3=findViewById(R.id.date);
        tv4=findViewById(R.id.time);
        tv5=findViewById(R.id.category);
        b=findViewById(R.id.star);

        Intent intent = getIntent();

        bundle = intent.getExtras();
        Long id=bundle.getLong(MainActivity.ID);


       /* if(bundle != null){

            String title = bundle.getString(MainActivity.TITLE);
            String desc= bundle.getString(MainActivity.DESCRIPTION);
            String time = bundle.getString(MainActivity.DATE);
            String date = bundle.getString(MainActivity.TIME);

        }*/


        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String[] selectionArgument = {id + ""};
        //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

        Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_ID + " = ? ", selectionArgument, null, null, null, null);
        while(cursor.moveToNext()){

            title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));
            category = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));
            mark= cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_MARK));

        }


        tv1.setText(title);
        tv2.setText(desc);
        tv3.setText(date);
        tv4.setText(time);
        tv5.setText(category);

        if(mark == 1)
            b.setBackground(this.getResources().getDrawable(R.drawable.starblue3));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAILS_REQUEST_CODE && resultCode == EditActivity.EDIT_EXPENSE_RESULT_CODE) {

            if (data != null) {
                bundle = data.getExtras();
                long id=bundle.getLong(MainActivity.ID);

                //Toast.makeText(this, "activity on result called", Toast.LENGTH_SHORT).show();

               /* if (bundle != null) {

                    String title = bundle.getString(MainActivity.TITLE);
                    String desc = bundle.getString(MainActivity.DESCRIPTION);
                    String time = bundle.getString(MainActivity.TIME);
                    String date = bundle.getString(MainActivity.DATE);*/


                    ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase database = openHelper.getReadableDatabase();

                    String[] selectionArgument = {id + ""};
                    //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

                    Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_ID + " = ? ", selectionArgument, null, null, null, null);
                    while(cursor.moveToNext()){

                        title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                        desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                        date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                        time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));
                        category= cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));

                    }


                    tv1.setText(title);
                    tv2.setText(desc);
                    tv3.setText(date);
                    tv4.setText(time);
                    tv5.setText(category);

                    setResult(DETAILS_RESULT_CODE,data);
                    finish();

                }

        }


        if(requestCode == DETAILS_REQUEST_CODE && resultCode == EditActivity.EDIT_EXPENSE_NOT_CHANGE_RESULT_CODE) {

            if (data != null) {
                bundle = data.getExtras();
                long id = bundle.getLong(MainActivity.ID);

                //Toast.makeText(this, "activity on result called", Toast.LENGTH_SHORT).show();

               /* if (bundle != null) {

                    String title = bundle.getString(MainActivity.TITLE);
                    String desc = bundle.getString(MainActivity.DESCRIPTION);
                    String time = bundle.getString(MainActivity.TIME);
                    String date = bundle.getString(MainActivity.DATE);*/


                ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getReadableDatabase();

                String[] selectionArgument = {id + ""};
                //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

                Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_ID + " = ? ", selectionArgument, null, null, null, null);
                while (cursor.moveToNext()) {

                    title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                    desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                    date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                    time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));
                    category = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));

                }


                tv1.setText(title);
                tv2.setText(desc);
                tv3.setText(date);
                tv4.setText(time);
                tv5.setText(category);

                setResult(DETAILS_RESULT_NOT_CHANGE_CODE, data);
                finish();

            }
        }

    }

    public  void edit(View v)
    {

        Intent  intent = new Intent(this,EditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,DETAILS_REQUEST_CODE);

    }

}
