package com.ariellevit.welldone;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<Food> {

    public static class ViewHolder{
        TextView name;

    }
    public FoodAdapter(Context context, ArrayList<Food> foods) {
        super(context,0, foods);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        //Get the item date for this position
        Food food = getItem(position);

        ViewHolder ViewHolder;

        //Check if an existing view is being reused, otherwise inflate a new view from row layout
        if (convertView == null){
            ViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);

            ViewHolder.name = (TextView) convertView.findViewById(R.id.listName);

            convertView.setTag(ViewHolder);
        }else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }

        ViewHolder.name.setText(food.getName());



        return convertView;
    }


}
