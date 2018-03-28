package com.example.administrator.hikiateweb.Util;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.View.MainActivity;

/**
 * Created by Administrator on 2018/03/27.
 */

public final class Init {
    public static void initPage(MainActivity activity) {
        //EditText
        //focus
        EditText kokban_text = activity.findViewById(R.id.kokban_text);
        kokban_text.setFocusableInTouchMode(true);
        kokban_text.setFocusable(true);
        kokban_text.requestFocus();

        EditText[] arr_edittext = new EditText[] {kokban_text
                                                , activity.findViewById(R.id.hinban_text)
                                                , activity.findViewById(R.id.lotno_text)
                                                , activity.findViewById(R.id.carbon_text)};
        for (EditText e : arr_edittext) { e.setText(""); }

        //TextView
        TextView[] arr_textview = new TextView[] {activity.findViewById(R.id.c1)
                                                , activity.findViewById(R.id.c2)
                                                , activity.findViewById(R.id.c3)
                                                , activity.findViewById(R.id.c4)
                                                , activity.findViewById(R.id.c5)
                                                , activity.findViewById(R.id.c6)
                                                , activity.findViewById(R.id.l1)
                                                , activity.findViewById(R.id.l2)
                                                , activity.findViewById(R.id.l3)
                                                , activity.findViewById(R.id.l4)
                                                , activity.findViewById(R.id.l5)
                                                , activity.findViewById(R.id.l6)};
        for (TextView t : arr_textview) { t.setText(""); }
        //msg
        TextView msg_text = activity.findViewById(R.id.msg_text);
        msg_text.setText(Constants.MSG_STR);

        //Button
        Button upd_btn = activity.findViewById(R.id.upd_btn);
        upd_btn.setEnabled(false);

        //DataHikiate
        activity.setDataHikiate(null);
    }
}
