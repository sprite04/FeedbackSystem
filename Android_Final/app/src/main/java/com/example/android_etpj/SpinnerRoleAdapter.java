package com.example.android_etpj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerRoleAdapter extends ArrayAdapter<String> {
    public SpinnerRoleAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_login_selected,parent,false);
        TextView tvSelected = convertView.findViewById(R.id.tv_selected);

        Object object=this.getItem(position);
        if(object!=null){
            tvSelected.setText(object.toString());
        }
        return convertView;
    }



    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_role,parent,false);
        TextView tvRole=convertView.findViewById(R.id.tv_role);

        Object object=this.getItem(position);
        if(object!=null){
            tvRole.setText(object.toString());
        }
        return convertView;
    }


}
