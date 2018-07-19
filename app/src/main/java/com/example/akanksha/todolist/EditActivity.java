package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText titleEditText,descEditText,dateEditText,timeEditText;
    String titleExpense,descExpense,dateExpense,timeExpense;
    Bundle bundle;
    public static final int EDIT_EXPENSE_RESULT_CODE = 1013;
    public static final int EDIT_EXPENSE_NOT_CHANGE_RESULT_CODE = 1015;
    String time;
    String date;
    String title;
    String desc;
    String category;
    String categoryExpense;
    int mark;
    long id;
    long code;
    CheckBox checkBox;
    ArrayList<String> categories;
    Spinner spinner;
    int catselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

*/
        titleEditText = findViewById(R.id.edittitle);
        descEditText = findViewById(R.id.editdesc);
        dateEditText = findViewById(R.id.date);
        timeEditText = findViewById(R.id.time);
        checkBox =findViewById(R.id.check);



        Intent intent = getIntent();

        bundle = intent.getExtras();
        id=bundle.getLong(MainActivity.ID);
        code = bundle.getLong("code",0);

        spinner=findViewById(R.id.spin);
        spinner.setOnItemSelectedListener(this);

        categories = new ArrayList<>();
        categories.add("Default");
        categories.add("Shopping");
        categories.add("Home");
        categories.add("Personal");
        categories.add("Work");

        ArrayAdapter<String> catadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);

        spinner.setAdapter(catadapter);



       /* if(bundle != null){

            code=bundle.getLong("code",0);
            String title = bundle.getString(MainActivity.TITLE);
            String desc= bundle.getString(MainActivity.DESCRIPTION);
            time = bundle.getString(MainActivity.TIME);
            date = bundle.getString(MainActivity.DATE);


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
            category =cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_CATEGORY));
            mark = cursor.getInt(cursor.getColumnIndex(Contract.Item.COLUMN_MARK));

        }


        titleEditText.setText(title);
        descEditText.setText(desc);
        dateEditText.setText(date);
        timeEditText.setText(time);

        if(category.equals("Default"))
            catselected=0;
        else if(category.equals("Shopping"))
            catselected=1;
        else if(category.equals("Home"))
            catselected=2;
        else if(category.equals("Personal"))
            catselected=3;
        else if(category.equals("Work"))
            catselected=4;


        spinner.setSelection(catselected);

        if(mark == 1)
         checkBox.setChecked(true);
        else
          checkBox.setChecked(false);



        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();

            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save){

            if(validateFields()){

               // Intent intent = new Intent();

                /*bundle.putString(MainActivity.TITLE,titleExpense);
                bundle.putString(MainActivity.DESCRIPTION,descExpense);
                bundle.putString(MainActivity.DATE,dateExpense);
                bundle.putString(MainActivity.TIME,timeExpense);
                bundle.putLong(MainActivity.ID,id);
                intent.putExtras(bundle);

                setResult(EDIT_EXPENSE_RESULT_CODE,intent);
                finish();*/

                int markvalue;

                if(checkBox.isChecked() == true)
                    markvalue=1;
                else
                    markvalue=0;


                Item expense = new Item(titleExpense,descExpense,dateExpense,timeExpense,category,markvalue,0);
                    expense.setId(id);

                    ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Item.COLUMN_TITLE,expense.getTitle());
                    contentValues.put(Contract.Item.COLUMN_DESC,expense.getDescription());
                    contentValues.put(Contract.Item.COLUMN_DATE,expense.getDate());
                    contentValues.put(Contract.Item.COLUMN_TIME,expense.getTime());
                    contentValues.put(Contract.Item.COLUMN_CATEGORY,expense.getCategory());
                    contentValues.put(Contract.Item.COLUMN_MARK,expense.getMark());
                    contentValues.put(Contract.Item.COLUMN_CHECK,expense.getCheck());

                   String[] selectionArgs= {id + ""};
                   database.update(Contract.Item.TABLE_NAME,contentValues,Contract.Item.COLUMN_ID + " = ? ",selectionArgs);


                if( !expense.getTime().equals(time) || !expense.getDate().equals(date) ) {

                    Bundle bundle1 = new Bundle();

                    int rcode = (int) id;

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent editintent = new Intent(this, NotificationReve.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rcode, editintent, 0);
                    alarmManager.cancel(pendingIntent);


                    int r = rcode + 100;
                    Intent intent1 = new Intent(this, NotificationReve.class);
                    bundle1.putLong(MainActivity.ID, rcode);

                    intent1.putExtras(bundle1);
                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, r, intent1, 0);

                    String[] d = expense.getDate().split("-");
                    String[] t = expense.getTime().split(":");

                    int yr = Integer.parseInt(d[2]);
                    int mon = Integer.parseInt(d[1]);
                    int da = Integer.parseInt(d[0]);
                    int h = Integer.parseInt(t[0]);
                    int min = Integer.parseInt(t[1]);


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yr, mon - 1, da, h, min - 1);

                    //long currentTime = System.currentTimeMillis();
                    long l = calendar.getTimeInMillis();
                    //long l1=l-2000000000L;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, l, pendingIntent1);

                }


                if(code == 2) {

                Intent data = new Intent();

                Bundle bundle = new Bundle();

                bundle.putLong(MainActivity.ID,id);
                data.putExtras(bundle);

                if(title.equals(titleExpense) && desc.equals(descExpense) && date.equals(dateExpense) && time.equals(timeExpense) && category.equals(categoryExpense) && mark==markvalue)
                    setResult(EDIT_EXPENSE_NOT_CHANGE_RESULT_CODE,data);
                else
                    setResult(EDIT_EXPENSE_RESULT_CODE,data);

                finish();

                }
                else{

                    Intent intent2 = new Intent(this, MainActivity.class);
                    startActivity(intent2);
                    finish();

                }


            }
            else{

                Toast.makeText(EditActivity.this, "Incomplete details", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }


    private boolean validateFields() {

        titleExpense = titleEditText.getText().toString().trim();
        descExpense = descEditText.getText().toString().trim();
        dateExpense = dateEditText.getText().toString();
        timeExpense = timeEditText.getText().toString();

        if(titleExpense.isEmpty()){

            titleEditText.setError("Enter title");
            return false;
        }

        if(descExpense.isEmpty()){

            descEditText.setError("Enter amount");
            return false;
        }

        if(dateExpense.isEmpty()){

            dateEditText.setError("Select date");
            return false;
        }

        if(timeExpense.isEmpty()){

            timeEditText.setError("Select time");
            return false;
        }

        return true;
    }

    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String [] t=time.split(":");
        int h=Integer.parseInt(t[0]);
        int m=Integer.parseInt(t[1]);


        TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeExpense = hourOfDay + ":" + minute;
                timeEditText.setText(timeExpense);

            }
        },h,m,false);

        timePickerDialog.show();
    }

    private void setDate() {


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String [] d=date.split("-");

        int yr= Integer.parseInt(d[2]);
        int mon= Integer.parseInt(d[1]);
        int da=Integer.parseInt(d[0]);


        DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                ++month;
                dateExpense = dayOfMonth + "-" + month + "-" + year;
                dateEditText.setText(dateExpense);
            }
        },yr,mon-1,da);

        datePickerDialog.show();
    }

    public  void calendar(View v)
    {
        setDate();
    }

    public  void clock(View v)
    {
        setTime();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

         categoryExpense= categories.get(i);
       // textView.setText(c);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
