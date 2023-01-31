package com.example.vehiclefinanceprocessing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.dealerlistlayout,null);
        TextView name,email;
        ImageView imageView ;

        name = view.findViewById(R.id.DealerName);
        email = view.findViewById(R.id.DealerEmail);
        imageView = view.findViewById(R.id.DealerImage);
        TextView StatusView = view.findViewById(R.id.usrStatus);
        name.setText(arr.get(i).getName());
        email.setText(arr.get(i).getEmailAddress());
        String url = arr.get(i).getImage();
        String Status = arr.get(i).getStatus();
        if (Status.toLowerCase().equals("active")){
            StatusView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.Active));
        }else{
            StatusView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.Danger));
        }
        if (!url.equals("") && !url.equals(null)){
            Picasso.get().load(url).into(imageView);
        }else {
            imageView.setImageResource(R.drawable.usericon);
        }
        return view;
    }

}
