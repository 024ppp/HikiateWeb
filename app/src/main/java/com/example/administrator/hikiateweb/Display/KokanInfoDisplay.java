package com.example.administrator.hikiateweb.Display;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.example.administrator.hikiateweb.R;

/**
 * Created by Administrator on 2018/03/13.
 */

public class KokanInfoDisplay extends Display{
    EditText kokban_text, hinban_text, lotno_text, carbon_text;

    public KokanInfoDisplay(Activity activity) {
        this.kokban_text = activity.findViewById(R.id.kokban_text);
        this.hinban_text = activity.findViewById(R.id.hinban_text);
        this.lotno_text = activity.findViewById(R.id.lotno_text);
        this.carbon_text = activity.findViewById(R.id.carbon_text);
    }

    @Override
    public void setMsg(String... msgs) {
        kokban_text.setText(msgs[0]);
        hinban_text.setText(msgs[1]);
        lotno_text.setText(msgs[2]);
        carbon_text.setText(msgs[3]);
    }
}
