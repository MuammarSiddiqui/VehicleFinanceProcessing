package com.example.vehiclefinanceprocessing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.application_view,null);
        TextView name = view.findViewById(R.id.FinanceUserName);
        TextView carname = view.findViewById(R.id.FinanceCarName);
        TextView amount = view.findViewById(R.id.FinanceAmount);
        TextView status = view.findViewById(R.id.statustext);
        ImageView iview = view.findViewById(R.id.statusimg);
        name.setText(arr.get(i).getUserName());
        carname.setText(arr.get(i).getVehicleName());
        amount.setText(arr.get(i).getAmountOfFinance());
        if (arr.get(i).getStatus().equals("Applied")){
            iview.setImageResource(R.mipmap.pending);
            status.setText("WAITING FOR APPROVAL");
        } else if (arr.get(i).getStatus().equals("Approved")) {
            iview.setImageResource(R.mipmap.approved);
            status.setTextColor(R.color.Active);
            status.setText("Approved by " + arr.get(i).getDealerName());

        } else if (arr.get(i).getStatus().equals("Rejected")) {
            iview.setImageResource(R.mipmap.rejected);
            status.setTextColor(R.color.Danger);
            status.setText("Rejected by " + arr.get(i).getDealerName());
        }
        return view;
    }
}
