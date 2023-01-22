package com.example.vehiclefinanceprocessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.ActionBarContextView;

public class GridAdapter extends BaseAdapter {
    Context context;
    String[] Id;
    String[] CarName;
    String[] CarPrice;
    int[] Images;
    LayoutInflater inflater;

    public String[] getId() {
        return Id;
    }

    public void setId(String[] id) {
        Id = id;
    }

    public GridAdapter(Context context, String[] carName, String[] carPrice, int[] images, String [] id) {
        this.context = context;
        CarName = carName;
        Images = images;
        CarPrice = carPrice;
        Id =id;
    }

    @Override
    public int getCount() {
        return CarName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null){
            view = inflater.inflate(R.layout.list_item_car,null);
        }
        ImageView imageview = view.findViewById(R.id.carImage);
        TextView name= view.findViewById(R.id.carName);
        TextView price= view.findViewById(R.id.carPrice);
        imageview.setImageResource(Images[i]);
        name.setText(CarName[i]);
        price.setText(CarPrice[i]);
    return  view;
    }
}
