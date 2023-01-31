package com.example.vehiclefinanceprocessing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CarsListAdapter extends BaseAdapter {


    Context context;


    ArrayList<Cars> arr;

    public CarsListAdapter(Context context, ArrayList<Cars> arr) {
        this.context = context;
        this.arr = arr;
    }
    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_car,null);
        TextView price,name;
        ImageView imageView ;

        price = view.findViewById(R.id.carPrice);
        name = view.findViewById(R.id.carName);
        imageView = view.findViewById(R.id.carImage);
        price.setText(arr.get(i).getPrice());
        name.setText(arr.get(i).getName());
        String url = arr.get(i).getImage();

        Picasso.get().load(url).into(imageView);
        return view;
    }





}
