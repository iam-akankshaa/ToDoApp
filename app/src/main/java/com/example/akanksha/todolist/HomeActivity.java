package com.example.akanksha.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {


    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    Bundle bundle;
    public static final int DETAILS_REQUEST_CODE = 22;
    public static final int DETAILS_RESULT_CODE = 1012;

    String title;
    String desc;
    String date;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv1=findViewById(R.id.title);
        tv2=findViewById(R.id.desc);
        tv3=findViewById(R.id.date);
        tv4=findViewById(R.id.time);

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

        }


        tv1.setText(title);
        tv2.setText(desc);
        tv3.setText(date);
        tv4.setText(time);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAILS_REQUEST_CODE && resultCode == EditActivity.EDIT_EXPENSE_RESULT_CODE) {

            if (data != null) {
                bundle = data.getExtras();
                long id=bundle.getLong(MainActivity.ID);

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

                    }


                    tv1.setText(title);
                    tv2.setText(desc);
                    tv3.setText(date);
                    tv4.setText(time);

                    setResult(DETAILS_RESULT_CODE,data);
                    finish();

                }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.edit){

            Intent  intent = new Intent(this,EditActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,DETAILS_REQUEST_CODE);

        }

        return true;

    }


}
