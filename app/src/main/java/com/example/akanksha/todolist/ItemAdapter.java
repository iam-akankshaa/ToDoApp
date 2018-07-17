package com.example.akanksha.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter{

    ArrayList<Item> item;
    LayoutInflater inflater;
    private TextView findViewByID;

    public ItemAdapter(@NonNull Context context, ArrayList<Item> items) {

        super(context,0,items);

        inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        this.item=items;

    }

    public int getCount()
    {
        return item.size();
    }

    public View getView(int position, View convert, ViewGroup parent)
    {

        View output = inflater.inflate(R.layout.row_layout,parent,false);

        TextView tv1=output.findViewById(R.id.itemtitle);
        TextView tv2=output.findViewById(R.id.itemdescription);
        TextView tv3=output.findViewById(R.id.itemdate);
        TextView tv4=output.findViewById(R.id.itemtime);

        Item i=item.get(position);
        tv1.setText(i.getTitle());
        tv2.setText(i.getDescription());
        tv3.setText(i.getDate());
        tv4.setText(i.getTime());

         return  output;
    }



}
