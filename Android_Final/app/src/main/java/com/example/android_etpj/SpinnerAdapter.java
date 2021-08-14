package com.example.android_etpj;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Object> {

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Object> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_selected,parent,false);
        TextView tvSelected=convertView.findViewById(R.id.tv_selected);

        Object object=this.getItem(position);
        if(object!=null){
            tvSelected.setText(object.toString());
        }
        return convertView;
    }



    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_category,parent,false);
        TextView tvCategory=convertView.findViewById(R.id.tv_category);

        Object object=this.getItem(position);
        if(object!=null){
            tvCategory.setText(object.toString());
        }
        return convertView;
    }
}
