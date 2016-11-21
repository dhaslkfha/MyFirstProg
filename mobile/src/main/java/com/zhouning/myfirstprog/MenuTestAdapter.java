package com.zhouning.myfirstprog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 宁 on 2016/9/22.
 */
public class MenuTestAdapter extends BaseAdapter{
    ArrayList<String> menuNames;
    ArrayList<Drawable> menuImages;
    Context context;
    ViewHolder holder;
    MenuTestAdapter(Context context, ArrayList<Drawable> menuImages,ArrayList<String> menuNames){
        this.context = context;
        this.menuImages = menuImages;
        this.menuNames = menuNames;
    }
    @Override
    public int getCount() {
        return menuImages.size();
    }

    @Override
    public Object getItem(int position) {
        return menuImages.get(position);
    }

    public Drawable getDrawableItem(int position){
        return menuImages.get(position);
    }
    public String getNameItem(int position){
        return menuNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gridmenu_item,null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.image);
            holder.textView = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.imageView.setImageDrawable(getDrawableItem(position));
        holder.textView.setText(getNameItem(position));
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
