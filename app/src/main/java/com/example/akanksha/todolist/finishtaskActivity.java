package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class finishtaskActivity extends AppCompatActivity implements  AdapterView.OnItemLongClickListener{

    ListView l;
    ArrayList<Item> items;
    LayoutInflater inflater;
    FinishtaskAdapter adapter;
    Bundle bundle;
    public static final int FINISH_RESULT_CODE = 1019;
    FrameLayout rootlayout;
    LinearLayout initialayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishtask);

        l=findViewById(R.id.list);
        items=new ArrayList<>();
        rootlayout=findViewById(R.id.root);

        //inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        adapter = new FinishtaskAdapter(this, items);

        l.setAdapter(adapter);

        //l.setOnItemClickListener(this);
        l.setOnItemLongClickListener(this);
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
            checkdb(items);

            Toast.makeText(this,"Finished Tasks Deleted",Toast.LENGTH_LONG).show();

        }


    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final Item expense = items.get(i);
        final int position =i;

        AlertDialog.Builder builder = new AlertDialog.Builder(finishtaskActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Do You Really Want To Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                long id = expense.getId();
                String[] selectionArgs = {id + ""};

                database.delete(Contract.Item.TABLE_NAME,Contract.Item.COLUMN_ID + " = ?",selectionArgs);
                items.remove(position);

                adapter.notifyDataSetChanged();
                Toast.makeText(finishtaskActivity.this,"Task Deleted",Toast.LENGTH_LONG).show();
                checkdb(items);

                int rcode=(int) id;

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(finishtaskActivity.this,NotificationReve.class);
                PendingIntent pendingIntent =  PendingIntent.getBroadcast(finishtaskActivity.this,rcode,intent,0);
                alarmManager.cancel(pendingIntent);


               // Intent intent1 = new Intent(finishtaskActivity.this,MainActivity.class);
               // startActivity(intent1);


            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog =builder.create();
        dialog.show();

        return false;
    }


    public void displayAll()
    {
        items.clear();

        if (rootlayout.indexOfChild(initialayout) > -1) {
            // Remove initial layout if it's previously added
            rootlayout.removeView(initialayout);
        }

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
        checkdb(items);

    }

    public void onBackPressed() {

        setResult(FINISH_RESULT_CODE);
        finish();

    }

    public void checkdb(ArrayList<Item> items)
    {
        if(items.isEmpty())
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            initialayout = (LinearLayout) inflater.inflate(R.layout.finish_initial_layout,rootlayout,false);
            rootlayout.addView(initialayout);
        }
    }


}
