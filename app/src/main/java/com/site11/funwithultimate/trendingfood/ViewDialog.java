package com.site11.funwithultimate.trendingfood;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDialog {

    public void showDialog(final Activity activity1, String msg){
        final Dialog dialog1 = new Dialog(activity1);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialogbox_otp);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog1.findViewById(R.id.txt_file_path);
        text.setText(msg);

        Button dialogBtn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make toast
                Toast.makeText(activity1.getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
            }
        });

        Button dialogBtn_okay = (Button) dialog1.findViewById(R.id.btn_okay);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity1.getApplicationContext(),"Good Buy, See You Later...",Toast.LENGTH_LONG).show();
                activity1.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        dialog1.show();
    }
}