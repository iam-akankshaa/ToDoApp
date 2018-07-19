package com.example.akanksha.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MyBootReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {


        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(context);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.Item.TABLE_NAME,null,  null,null,null,null,null);
        while(cursor.moveToNext()) {

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Item.COLUMN_ID));
            String date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));


            int rcode=(int) id;
            Bundle bundle1= new Bundle();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent1 = new Intent(context, NotificationReve.class);

            bundle1.putLong(MainActivity.ID,id);

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

        cursor.close();
    }
}
