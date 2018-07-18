package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    public  static  final  int RESULT_CODE=2;
    static  String senderNum;
    static  String message;
    static  String time;
    static  String date;

    //ArrayList<Item> items;
    LayoutInflater inflater;
    //ItemAdapter adapter;

    public static final String TITLE_KEY = "title";
    public static final String DESC_KEY = "desc";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        //items = new ArrayList<>();

        //adapter = new ItemAdapter(this,items);

        //l.setAdapter(adapter);
        final Bundle bundle = intent.getExtras();
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
                    //mobile=senderNum.replaceAll("\\s","");
                    //body=message.replaceAll("\\s","+");

                    String format = "HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                    time = sdf.format(Calendar.getInstance().getTime());
                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }

        /*
        Bundle b = new Bundle();
        b.putString(TITLE_KEY,senderNum);
        b.putString(DESC_KEY, message);
        b.putString(DATE_KEY,date);
        b.putString(TIME_KEY,time);

        intent.setClass(context,MainActivity.class);
        intent.putExtras(b);
        context.startActivity(intent); */

        /*String title = data.getStringExtra(TITLE);
        String desc = data.getStringExtra(DESCRIPTION);
        String date=data.getStringExtra(DATE);
        String time= data.getStringExtra(TIME);
        //int amount = Integer.parseInt(amountString);*/
        Item expense = new Item(senderNum, message, date, time,"DEFAULT",0,0);

        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(context);
        SQLiteDatabase database = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Item.COLUMN_TITLE, expense.getTitle());
        contentValues.put(Contract.Item.COLUMN_DESC, expense.getDescription());
        contentValues.put(Contract.Item.COLUMN_DATE, expense.getDate());
        contentValues.put(Contract.Item.COLUMN_TIME, expense.getTime());
        contentValues.put(Contract.Item.COLUMN_CATEGORY,expense.getCategory());
        contentValues.put(Contract.Item.COLUMN_MARK,expense.getMark());
        contentValues.put(Contract.Item.COLUMN_CHECK,expense.getCheck());

        long id=database.insert(Contract.Item.TABLE_NAME, null, contentValues);
        /*if (id > -1L) {
            expense.setId(id);

            items.add(expense);
            adapter.notifyDataSetChanged();


        }*/

        int rcode=(int) id;
        Bundle bundle1= new Bundle();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(context, NotificationReve.class);

        bundle1.putLong(MainActivity.ID,id);
        bundle1.putString(MainActivity.TITLE, senderNum);
        bundle1.putString(MainActivity.DESCRIPTION,message);
        bundle1.putString(MainActivity.DATE,date);
        bundle1.putString(MainActivity.TIME,time);

        intent1.putExtras(bundle1);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context,rcode,intent1,0);

        String [] d=  date.split("-");
        String [] t = time.split(":");

        int yr= Integer.parseInt(d[2]);
        int mon= Integer.parseInt(d[1]);
        int da=Integer.parseInt(d[0]);
        int h=Integer.parseInt(t[0]);
        int min=Integer.parseInt(t[1]);


        Calendar calendar = Calendar.getInstance();
        calendar.set(yr,mon-1,da,h,min+2);

        long currentTime = System.currentTimeMillis();
        long l=calendar.getTimeInMillis();
        //long l1=l-2000000000L;
        alarmManager.set(AlarmManager.RTC_WAKEUP,l,pendingIntent);

        Log.d("MyReceiver","alarm");



    }

}
