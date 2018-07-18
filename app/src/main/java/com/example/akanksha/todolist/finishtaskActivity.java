package com.example.akanksha.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class finishtaskActivity extends AppCompatActivity {

    ListView l;
    ArrayList<Item> items;
    LayoutInflater inflater;
    FinishtaskAdapter adapter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishtask);

        l=findViewById(R.id.list);
        items=new ArrayList<>();

        //inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        adapter = new FinishtaskAdapter(this, items);

        l.setAdapter(adapter);

        //l.setOnItemClickListener(this);
        //l.setOnItemLongClickListener(this);
        Log.d("Main ACtivity","oncreate");

        Intent intent = getIntent();
        bundle = intent.getExtras();
        int id=bundle.getInt("code");

        if(id == 0)
        {
            displayAll();

        }

        else
        {
            items.clear();
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getWritableDatabase();

            String[] selectionArgs = {1 + ""};

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_CHECK + " = ? ", selectionArgs, null, null, null, null);
            while(cursor.moveToNext()){

                database.delete(Contract.Item.TABLE_NAME,Contract.Item.COLUMN_CHECK + " = ?",selectionArgs);

            }

            cursor.close();
            adapter.notifyDataSetChanged();

            Toast.makeText(this,"Finished Tasks Deleted",Toast.LENGTH_LONG).show();

        }


    }


    public void displayAll()
    {
        items.clear();
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String[] selectionArgument = { 1 + ""};
        //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

        Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_CHECK + " = ? ", selectionArgument, null, null, null, null);
        while(cursor.moveToNext()){

            String  title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));
            String category =cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));
            int mark = cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_MARK));
            int check = cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_CHECK));

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

            Item expense = new Item(title,desc,date,time,category,mark,check);
            expense.setId(id);
            items.add(expense);

        }
        cursor.close();
        adapter.notifyDataSetChanged();

    }

}
