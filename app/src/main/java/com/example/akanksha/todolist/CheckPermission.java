package com.example.akanksha.todolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class CheckPermission extends AppCompatActivity {

    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        check = (CheckBox) findViewById(R.id.check);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){

            check.setChecked(true);

        }


    }


    public void onClick(View v)
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {

            check.setChecked(true);
            check.setEnabled(false);
            Toast.makeText(this, "Permission Already Granted ", Toast.LENGTH_LONG).show();



        }

        else
        {
            String[] permissions = {Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this,permissions,1);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){

            int callGrantResult = grantResults[0];
            if(callGrantResult == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Permission Granted ",Toast.LENGTH_LONG).show();
                check.setChecked(true);
            }
            else {

                Toast.makeText(this,"Grant permission",Toast.LENGTH_LONG).show();
                check.setChecked(false);
            }

        }



    }




}
