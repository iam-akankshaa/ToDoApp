package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    public static final int FINISH_REQUEST_CODE = 101;


    FrameLayout rootlayout;
    LinearLayout initialayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        l=findViewById(R.id.list);
        items=new ArrayList<>();
        rootlayout=findViewById(R.id.root);

        //inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;


        adapter = new ItemAdapter(this, items, new MarkClickListener() {
            @Override
            public void markButtonClicked(Item item, int position) {

                long id= item.getId();

                if(item.getMark() == 0) {
                    item.setMark(1);

                    ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Item.COLUMN_MARK, item.getMark());

                    String[] selectionArgs = {id + ""};
                    database.update(Contract.Item.TABLE_NAME, contentValues, Contract.Item.COLUMN_ID + " = ? ", selectionArgs);
                }

                else
                {
                    item.setMark(0);
                    ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Item.COLUMN_MARK, item.getMark());

                    String[] selectionArgs = {id + ""};
                    database.update(Contract.Item.TABLE_NAME, contentValues, Contract.Item.COLUMN_ID + " = ? ", selectionArgs);

                }

                displayUncheck();


            }
        }, new checkClickListener(){

            public  void checkButtonClicked(Item item, int position){

                long id= item.getId();
                item.setCheck(1);

                ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Item.COLUMN_CHECK,item.getCheck());

                String[] selectionArgs= {id + ""};
                database.update(Contract.Item.TABLE_NAME,contentValues,Contract.Item.COLUMN_ID + " = ? ",selectionArgs);

                Toast.makeText(MainActivity.this,"Task Finished",Toast.LENGTH_LONG).show();

                displayUncheck();


            }
        }

        );

        l.setAdapter(adapter);


        l.setOnItemClickListener(this);
        l.setOnItemLongClickListener(this);
        Log.d("Main ACtivity","oncreate");

        //displayAll();
        displayUncheck();

        //Toast.makeText(MainActivity.this,"oncreate",Toast.LENGTH_LONG).show();



    }


    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("Main ACtivity","itemclick");

        Bundle bundle= new Bundle();
        Item it=items.get(i);

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
                Toast.makeText(MainActivity.this,"Task Deleted",Toast.LENGTH_LONG).show();
                checkdb(items);

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

    public void displayUncheck()
    {
        items.clear();

        if (rootlayout.indexOfChild(initialayout) > -1) {
            // Remove initial layout if it's previously added
            rootlayout.removeView(initialayout);
        }
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String[] selectionArgument = { 0 + ""};
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

    public void displayAll()
    {

        items.clear();

        if (rootlayout.indexOfChild(initialayout) > -1) {
            // Remove initial layout if it's previously added
            rootlayout.removeView(initialayout);
        }
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
            String cat= cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));
            int mark= cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_MARK));
            int check = cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_CHECK));

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

            Item expense = new Item(title,desc,date,time,cat,mark,check);
            expense.setId(id);
            items.add(expense);

        }
        cursor.close();
        adapter.notifyDataSetChanged();

        checkdb(items);
    }

    public void checkdb(ArrayList<Item> items)
    {
        if(items.isEmpty())
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            initialayout = (LinearLayout) inflater.inflate(R.layout.initial_layout,rootlayout,false);
            rootlayout.addView(initialayout);
        }
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
                displayUncheck();

                Toast.makeText(this, "Task Added", Toast.LENGTH_LONG).show();
                // TO Display Toast Need to do Start Activity For result


            }

        }

            if (requestCode == DETAILS_REQUEST_CODE && resultCode == HomeActivity.DETAILS_RESULT_CODE) {

                Bundle bundle;

                if (data != null) {

                    bundle = data.getExtras();

                    if (bundle != null) {

                        Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
                        displayUncheck();


                    }
                }

            }


        if (requestCode == DETAILS_REQUEST_CODE && resultCode == HomeActivity.DETAILS_RESULT_NOT_CHANGE_CODE) {

            Bundle bundle;

            if (data != null) {

                bundle = data.getExtras();

                if (bundle != null) {

                    Toast.makeText(this, "Task Not Modified", Toast.LENGTH_SHORT).show();
                    displayUncheck();


                }
            }

        }

        if (requestCode == FINISH_REQUEST_CODE) {

            if (resultCode == finishtaskActivity.FINISH_RESULT_CODE) {

                displayUncheck();


            }

        }


    }

    public  void add(View v)
    {
        Bundle bundle= new Bundle();
        bundle.putLong("code",1);

        Intent intent = new Intent(this,AddActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,ADD_REQUEST_CODE);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        int idmenu =item.getItemId();

        if(idmenu == R.id.all)
        {
            displayAll();
        }

        else if(idmenu == R.id.important)
        {
            items.clear();
            if (rootlayout.indexOfChild(initialayout) > -1) {
                // Remove initial layout if it's previously added
                rootlayout.removeView(initialayout);
            }
            ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();

            String[] selectionArgument = {1 + ""};
            //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

            Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_MARK + " = ? ", selectionArgument, null, null, null, null);
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

        else if(idmenu == R.id.dflt)
        {
            displayCategory("Default");
        }

        else if(idmenu == R.id.shopping)
        {
            displayCategory("Shopping");
        }

        else if(idmenu == R.id.home)
        {
            displayCategory("Home");
        }
        else if(idmenu == R.id.personal)
        {
            displayCategory("Personal");
        }

        else if(idmenu == R.id.work)
        {
            displayCategory("Work");
        }

        else if(idmenu == R.id.finished)
        {

           Intent intent = new Intent(this,finishtaskActivity.class);
           Bundle bundle = new Bundle();
           bundle.putInt("code",0);
           intent.putExtras(bundle);
           startActivityForResult(intent,FINISH_REQUEST_CODE);


        }


       else if(idmenu == R.id.delete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Are You Sure");
            builder.setMessage("Delete all finished tasks");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(MainActivity.this,finishtaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("code",1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,FINISH_REQUEST_CODE);

                }

            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog =builder.create();
            dialog.show();

        }

        else if(idmenu == R.id.sorttitle)
        {

            displaySort(Contract.Item.COLUMN_TITLE);

        }

       else if(idmenu == R.id.sortdesc)
        {

            displaySort(Contract.Item.COLUMN_DESC);

        }

       else  if(idmenu == R.id.sortdate)
        {
            displaySort(Contract.Item.COLUMN_DATE);

        }

        else if(idmenu == R.id.feedback)
        {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO); //Only For Mail

            Uri u =Uri.parse("mailto:akankshajain0209@gmail.com");

            intent.setData(u);
            startActivity(intent);

        }

        else if(idmenu == R.id.about)
        {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
        }


        else if(idmenu == R.id.settings)
       {
          Intent intent = new Intent(this,CheckPermission.class);
          startActivityForResult(intent,2);
       }


        return super.onOptionsItemSelected(item);
    }

    public  void displayCategory(String cat)
    {
        items.clear();
        if (rootlayout.indexOfChild(initialayout) > -1) {
            // Remove initial layout if it's previously added
            rootlayout.removeView(initialayout);
        }
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String[] selectionArgument = {cat};
        //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

        Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_CATEGORY + " = ? ", selectionArgument, null, null, null, null);
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

    public  void displaySort(String colname)
    {

        items.clear();
        if (rootlayout.indexOfChild(initialayout) > -1) {
            // Remove initial layout if it's previously added
            rootlayout.removeView(initialayout);
        }
        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, null, null, null, null, colname + " ASC", null);

        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));
            String cat= cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));
            int mark= cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_MARK));
            int check = cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_CHECK));


            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));

            Item expense = new Item(title,desc,date,time,cat,mark,check);
            expense.setId(id);
            items.add(expense);

        }
        cursor.close();
        adapter.notifyDataSetChanged();
        checkdb(items);


    }




}
