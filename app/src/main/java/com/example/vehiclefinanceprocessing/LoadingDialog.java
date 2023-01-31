package com.example.vehiclefinanceprocessing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity =  myActivity;
    }
    @SuppressLint("InflateParams")
    void StartloadingDialog(){
        AlertDialog.Builder Builder= new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        Builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        Builder.setCancelable(false);
        dialog = Builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
