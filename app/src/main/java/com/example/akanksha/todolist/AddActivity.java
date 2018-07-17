package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText ed1;
    EditText ed2;
    EditText dateEditText,timeEditText;
    String titleExpense,descExpense,dateExpense,timeExpense;
    Bundle bundle;
    int code;
    Spinner spinner;

    public static final int ADD_EXPENSE_RESULT_CODE = 1013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ed1= (EditText) findViewById(R.id.edittitle);
        ed2=(EditText) findViewById(R.id.editdesc);
        dateEditText = (EditText) findViewById(R.id.date);
        timeEditText = (EditText) findViewById(R.id.time);
        spinner=findViewById(R.id.spin);
       // spinner.setOnItemClickListener();


       /* ed1.setText(MyReceiver.senderNum);
        ed2.setText(MyReceiver.message);
        dateEditText.setText(MyReceiver.date);
        timeEditText.setText(MyReceiver.time);*/


      // if(ed2.getText() == null) {

           Intent intent = getIntent();
           String desc = intent.getStringExtra(Intent.EXTRA_TEXT);
           ed2.setText(desc);

       //}
         code=intent.getIntExtra("code",0);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate(dateEditText);

            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(timeEditText);
            }
        });


    }

    @Override
    public void onBackPressed() {

        titleExpense = ed1.getText().toString().trim();
        descExpense = ed2.getText().toString().trim();
        dateExpense = dateEditText.getText().toString();
        timeExpense = timeEditText.getText().toString();

        if(titleExpense.isEmpty() && dateExpense.isEmpty() && descExpense.isEmpty() && timeExpense.isEmpty())
        {
            finish();
        }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are You Sure?");
            builder.setMessage("Quit Without Saving");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu,menu);
        return  true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {


            // public void save(View v) {

            if (validateFields()) {


                Item expense = new Item(titleExpense, descExpense, dateExpense, timeExpense);

                ItemOpenHelper openHelper = ItemOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Item.COLUMN_TITLE, expense.getTitle());
                contentValues.put(Contract.Item.COLUMN_DESC, expense.getDescription());
                contentValues.put(Contract.Item.COLUMN_DATE, expense.getDate());
                contentValues.put(Contract.Item.COLUMN_TIME, expense.getTime());

                long id = database.insert(Contract.Item.TABLE_NAME, null, contentValues);
               /* if (id > -1L){
                    expense.setId(id);

                    items.add(expense);
                    adapter.notifyDataSetChanged();

                }*/

                int rcode = (int) id;
                Bundle bundle = new Bundle();

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(this, NotificationReve.class);
                bundle.putLong(MainActivity.ID, id);
                /*bundle.putString(MainActivity.TITLE, titleExpense);
                bundle.putString(MainActivity.DESCRIPTION,descExpense);
                bundle.putString(MainActivity.DATE,dateExpense);
                bundle.putString(MainActivity.TIME,timeExpense);*/
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rcode, intent, 0);

                String[] d = dateExpense.split("-");
                String[] t = timeExpense.split(":");

                int yr = Integer.parseInt(d[2]);
                int mon = Integer.parseInt(d[1]);
                int da = Integer.parseInt(d[0]);
                int h = Integer.parseInt(t[0]);
                int min = Integer.parseInt(t[1]);


                Calendar calendar = Calendar.getInstance();
                calendar.set(yr, mon - 1, da, h, min - 1);

                long currentTime = System.currentTimeMillis();
                long l = calendar.getTimeInMillis();
                //long l1=l-2000000000L;
                alarmManager.set(AlarmManager.RTC_WAKEUP, l, pendingIntent);


                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);


            } else {

                Toast.makeText(AddActivity.this, "Incomplete details", Toast.LENGTH_SHORT).show();
            }


        }

        return  true;

    }




    private boolean validateFields() {

        titleExpense = ed1.getText().toString().trim();
        descExpense = ed2.getText().toString().trim();
        dateExpense = dateEditText.getText().toString();
        timeExpense = timeEditText.getText().toString();

        if(titleExpense.isEmpty()){

            ed1.setError("Enter title");
            return false;
        }

        if(descExpense.isEmpty()){

            ed2.setError("Enter Description");
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

    private void setTime(View v) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeExpense = hourOfDay + ":" + minute;
                timeEditText.setText(timeExpense);

            }
        },hour,min,false);

        timePickerDialog.show();
    }

    private void setDate(View v) {


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                ++month;
                dateExpense = dayOfMonth + "-" + month + "-" + year;
                dateEditText.setText(dateExpense);
            }
        },year,month,day);

        datePickerDialog.show();
    }

}
