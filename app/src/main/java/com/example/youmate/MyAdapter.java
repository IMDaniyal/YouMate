package com.example.youmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<UrlModelData> arrayList;

    public MyAdapter( Context context, ArrayList<UrlModelData> arrayList ) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }


    @Override
    public Object getItem( int position ) {
        return arrayList.get(position);
    }


    @Override
    public long getItemId( int position ) {
        return position;
    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.bookmarklayout,null);
        TextView t1=convertView.findViewById(R.id.tvurl);

        UrlModelData urlModelData=arrayList.get(position);
        t1.setText(urlModelData.getUrl());

        return convertView;
    }
}
