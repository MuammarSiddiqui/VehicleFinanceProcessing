package com.example.vehiclefinanceprocessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ApplicationAdapter extends BaseAdapter {
    Context context;


    ArrayList<Applications> arr;

    public ApplicationAdapter() {
    }

    public ApplicationAdapter(Context context, ArrayList<Applications> arr) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.application_view,null);
        TextView name = view.findViewById(R.id.FinanceUserName);
        TextView carname = view.findViewById(R.id.FinanceCarName);
        TextView amount = view.findViewById(R.id.FinanceAmount);

        name.setText(arr.get(i).getUserName());
        carname.setText(arr.get(i).getVehicleName());
        amount.setText(arr.get(i).getAmountOfFinance());
        return view;
    }
}
