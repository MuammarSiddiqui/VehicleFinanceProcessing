package com.example.vehiclefinanceprocessing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity =  myActivity;
    }
    void StartloadingDialog(){
        AlertDialog.Builder Builder= new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        Builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        dialog = Builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
