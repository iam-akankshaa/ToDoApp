package com.example.akanksha.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter{

    ArrayList<Item> item;
    LayoutInflater inflater;
    private TextView findViewByID;
    MarkClickListener listener;
    checkClickListener clickListener;

    public ItemAdapter(@NonNull Context context, ArrayList<Item> items,MarkClickListener listener , checkClickListener clickListener){

        super(context,0,items);

        inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        this.item=items;
        this.listener=listener;
        this.clickListener=clickListener;

    }

    public int getCount()
    {
        return item.size();
    }

    public View getView(final int position, View convert, ViewGroup parent)
    {

        View output = inflater.inflate(R.layout.row_layout,parent,false);

        TextView tv1=output.findViewById(R.id.itemtitle);
        TextView tv2=output.findViewById(R.id.itemdescription);
        TextView tv3=output.findViewById(R.id.itemdate);
        TextView tv4=output.findViewById(R.id.itemtime);
        TextView tv5=output.findViewById(R.id.itemcategory);
        Button button = output.findViewById(R.id.star);
        CheckBox checkBox =output.findViewById(R.id.check);

        final Item i=item.get(position);
        tv1.setText(i.getTitle());
        tv2.setText(i.getDescription());
        tv3.setText(i.getDate());
        tv4.setText(i.getTime());
        tv5.setText(i.getCategory());

        if(i.getCheck() == 1) {
            checkBox.setChecked(true);

        }


        if(i.getMark() == 1) {
            button.setBackground(getContext().getResources().getDrawable(R.drawable.starblue));
            //button.setBackgroundColor(getContext().getResources().getColor(R.color.my_blue));

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.markButtonClicked(i,position);

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              clickListener.checkButtonClicked(i,position);

            }

        });

         return  output;

    }



}
