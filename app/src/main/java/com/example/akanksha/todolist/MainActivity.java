package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


  ListView l;
  ArrayList<Item> items;
  LayoutInflater inflater;
  ItemAdapter adapter;

    public static  final String TITLE="Title";
    public static  final String DESCRIPTION="Description";
    public static final String DATE= "date";
    public static final String TIME = "time";
    public static final String ID="id";

    public static final int ADD_REQUEST_CODE = 100;
    public static final int DETAILS_REQUEST_CODE = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        l=findViewById(R.id.list);
        items=new ArrayList<>();

        //inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        l.setOnItemClickListener(this);
        l.setOnItemLongClickListener(this);
       Log.d("Main ACtivity","oncreate");

        displayAll();

/*
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();
        //int amountGreaterThan = 0;
        //String[] selectionArgument = {amountGreaterThan + "",};
        String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};
        //Cursor cursor = database.query(Contract.Item.TABLE_NAME,columns, null,null,null,null,null);
        Cursor cursor = database.rawQuery("select * from " + Contract.Item.TABLE_NAME ,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

            Item expense = new Item(title,desc,date,time);
            expense.setId(id);
            items.add(expense);

        }
        cursor.close();


        adapter = new ItemAdapter(this,items);

        l.setAdapter(adapter);*/



    }


    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Bundle bundle= new Bundle();
        Item it=items.get(i);

       /* bundle.putString(TITLE,it.getTitle());
        bundle.putString(DESCRIPTION,it.getDescription());
        bundle.putString(DATE,it.getDate());
        bundle.putString(TIME,it.getTime());*/
        bundle.putLong(ID,it.getId());
        bundle.putLong("code",2);

        Intent intent =new Intent(MainActivity.this,HomeActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,DETAILS_REQUEST_CODE);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final Item expense = items.get(i);
        final int position =i;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                int rcode=(int) id;

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(MainActivity.this,NotificationReve.class);
                PendingIntent pendingIntent =  PendingIntent.getBroadcast(MainActivity.this,rcode,intent,0);
                alarmManager.cancel(pendingIntent);


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
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();
        //int amountGreaterThan = 0;
        //String[] selectionArgument = {amountGreaterThan + "",};
        String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};
        //Cursor cursor = database.query(Contract.Item.TABLE_NAME,columns, null,null,null,null,null);
        Cursor cursor = database.rawQuery("select * from " + Contract.Item.TABLE_NAME ,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

            Item expense = new Item(title,desc,date,time);
            expense.setId(id);
            items.add(expense);

        }
        cursor.close();


        adapter = new ItemAdapter(this,items);

        l.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

       /* if(requestCode == 1 && resultCode == 1)
        {

           String tit=  data.getStringExtra("TITLE");
           String desc=data.getStringExtra("DESCRIPTION");


            Item item = new Item(tit,desc);
            items.add(item);
            adapter=new ItemAdapter(MainActivity.this,items);
            l.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }*/
        Log.d("Main ACtivity", "onActivityResult");

        if (requestCode == ADD_REQUEST_CODE) {

            if (resultCode == AddActivity.ADD_EXPENSE_RESULT_CODE) {


                // displayAll();
                Toast.makeText(this, "Task Added", Toast.LENGTH_LONG).show();


            }

        }


            if (requestCode == DETAILS_REQUEST_CODE && resultCode == HomeActivity.DETAILS_RESULT_CODE) {

                Bundle bundle;

                if (data != null) {

                    bundle = data.getExtras();

                    if (bundle != null) {

                   /* String title = bundle.getString(MainActivity.TITLE);
                    String desc = bundle.getString(MainActivity.DESCRIPTION);
                    String time = bundle.getString(MainActivity.TIME);
                    String date = bundle.getString(MainActivity.DATE);
                    long id=bundle.getLong(MainActivity.ID);

                    Item expense = new Item(title,desc,date,time);
                    expense.setId(id*/


                        displayAll();


                    }
                }

            }




    }

    public  void add(View v)
    {
        Intent intent = new Intent(this,AddActivity.class);
        //intent.putExtra("code",1);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        int idmenu =item.getItemId();

        if(idmenu == R.id.add)
        {
            Intent intent = new Intent(this,AddActivity.class);
            //intent.putExtra("code",1);
            startActivity(intent);

            /*final View output= inflater.inflate(R.layout.alert_layout,l,false);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Add New ToDo");
            builder.setMessage("Enter Title and Message");
            builder.setView(output);
            builder.setCancelable(false);
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialogInterface, int i) {

                    EditText ed1= output.findViewById(R.id.edittitle);
                    EditText ed2= output.findViewById(R.id.editdesc);

                    String title= ed1.getText().toString();
                    String Description = ed2.getText().toString();

                    Item item= new Item(title,Description);
                    items.add(item);

                    adapter=new ItemAdapter(MainActivity.this,items);
                    l.setAdapter(adapter);


                }

            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO
                }
            })


            AlertDialog dialog = builder.create();
            dialog.show();*/



        }

        if(idmenu == R.id.settings)
        {
            Intent intent = new Intent(this,CheckPermission.class);
            startActivityForResult(intent,2);
        }

        if(idmenu == R.id.sorttitle)
        {

            items.clear();
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, null, null, null, null, Contract.Item.COLUMN_TITLE + " ASC", null);

            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

                long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

                Item expense = new Item(title,desc,date,time);
                expense.setId(id);
                items.add(expense);

            }
            cursor.close();


        }

        if(idmenu == R.id.sortdesc)
        {

            items.clear();
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, null, null, null, null, Contract.Item.COLUMN_DESC + " ASC", null);

            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

                long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

                Item expense = new Item(title,desc,date,time);
                expense.setId(id);
                items.add(expense);

            }
            cursor.close();

        }

        if(idmenu == R.id.sortdate)
        {

            items.clear();
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, null, null, null, null, Contract.Item.COLUMN_DATE + " ASC", null);

            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

                long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

                Item expense = new Item(title,desc,date,time);
                expense.setId(id);
                items.add(expense);

            }
            cursor.close();

        }

        if(idmenu == R.id.sorttime)
        {

            items.clear();
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, null, null, null, null, Contract.Item.COLUMN_TIME + " ASC", null);

            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
                String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

                long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

                Item expense = new Item(title,desc,date,time);
                expense.setId(id);
                items.add(expense);

            }
            cursor.close();

        }



        return super.onOptionsItemSelected(item);
    }




}
