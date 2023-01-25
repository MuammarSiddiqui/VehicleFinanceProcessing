package com.example.vehiclefinanceprocessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersListAdapter extends BaseAdapter {
    Context context;


    public UsersListAdapter(Context context, ArrayList<Users> arr) {
        this.context = context;
        this.arr = arr;
    }

    ArrayList<Users> arr= new ArrayList<>();
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
        view = LayoutInflater.from(context).inflate(R.layout.dealerlistlayout,null);
        TextView name,email;
        ImageView imageView ;

        name = view.findViewById(R.id.DealerName);
        email = view.findViewById(R.id.DealerEmail);
        imageView = view.findViewById(R.id.DealerImage);
        name.setText(arr.get(i).getName());
        email.setText(arr.get(i).getEmailAddress());
        String url = arr.get(i).getImage();

        if (!url.equals("") && !url.equals(null)){
            Picasso.get().load(url).into(imageView);
        }else {
            imageView.setImageResource(R.drawable.usericon);
        }
        return view;
    }

}
