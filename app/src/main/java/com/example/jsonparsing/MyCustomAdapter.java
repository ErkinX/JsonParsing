package com.example.jsonparsing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> list;
    private final Context context;

    // private final int viewid;

    public MyCustomAdapter(Context context, List<Contact> list) {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.list = list;
        // this.viewid = R.layout.rowlayout;
    }

    class ViewHolder {
        protected TextView name;
        protected TextView email;
        protected TextView mobile;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        LayoutInflater inflator = ((Activity) context).getLayoutInflater();
        view = inflator.inflate(R.layout.list_item, null);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.name = (TextView) view.findViewById(R.id.name);
        viewHolder.email = (TextView) view.findViewById(R.id.email);
        viewHolder.mobile = (TextView) view.findViewById(R.id.mobile);

        view.setTag(viewHolder);

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).name);
        holder.email.setText(list.get(position).email);
        holder.mobile.setText(list.get(position).phone.mobile);

        return view;
    }
}
