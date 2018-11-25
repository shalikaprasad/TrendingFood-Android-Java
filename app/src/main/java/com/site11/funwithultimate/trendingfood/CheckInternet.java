package com.site11.funwithultimate.trendingfood;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CheckInternet {

    public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.checkinternetlayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.checkinterner_path);
        text.setText(msg);

        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.internet_cancalbtn);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit
                activity.finish();
            }
        });

        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.internet_okaybtn);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open Setting panel
                activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                dialog.hide();
            }
        });

        dialog.show();
    }
}
