package com.example.akanksha.todolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReve extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Toast.makeText(context,"Alarm",Toast.LENGTH_LONG).show();

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("mychannelid","Expenses Channel",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);

        }

        Log.d("Notification","alarm");

        Bundle bundle = intent.getExtras();

        //String title = bundle.getString(MainActivity.TITLE);
        //String desc=intent.getStringExtra(MainActivity.DESCRIPTION);
        long id=bundle.getLong(MainActivity.ID,0);
        int rcode= (int)  id;
        String title="";
        String desc="";


        ItemOpenHelper openHelper = ItemOpenHelper.getInstance(context);
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String[] selectionArgument = {id + ""};
        //String[] columns = {Contract.Item.COLUMN_TITLE,Contract.Item.COLUMN_DESC, Contract.Item.COLUMN_DATE,Contract.Item.COLUMN_TIME,Contract.Item.COLUMN_ID};

        Cursor cursor = database.query(Contract.Item.TABLE_NAME, null, Contract.Item.COLUMN_ID + " = ? ", selectionArgument, null, null, null, null);
        while(cursor.moveToNext()){

            title = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TITLE));
            desc = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DESC));
            //date = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_DATE));
            //time = cursor.getString(cursor.getColumnIndex(Contract.Item.COLUMN_TIME));

        }


        String GROUP_KEY= "com.android.example.WORK_EMAIL";

        //String s=" Create an Expandable Notification A basic notification usually includes a title, a line of text, and one or more actions the user can perform in response. To provide even more information, you can also create large, expandable notifications by applying one of several notification templates as described on this page.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"mychannelid");
        builder.setContentTitle(title);
        builder.setContentText(desc);

        //builder.setSmallIcon(R.drawable.todoicon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.todoicon));

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(desc));
        builder.setGroup(GROUP_KEY);
        builder.setGroupSummary(true);
        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{250,250,250,250});
        //builder.setColor(context.getResources().getColor(R.color.colorPrimaryDark));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.todoicon);
            builder.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            builder.setSmallIcon(R.drawable.todoicon);

        }


        Intent intent1 = new Intent(context,HomeActivity.class);
        intent1.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,rcode,intent1,0);


        Intent intent2 = new Intent(context,EditActivity.class);
        intent2.putExtras(bundle);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context,rcode,intent2,0);
        builder.addAction(R.drawable.add,"EDIT",pendingIntent2);


        builder.setContentIntent(pendingIntent);
        android.app.Notification notification = builder.build();
        manager.notify(rcode,notification);



    }
}
