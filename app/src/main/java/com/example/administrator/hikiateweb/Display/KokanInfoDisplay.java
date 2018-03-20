package com.example.administrator.hikiateweb.Display;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.administrator.hikiateweb.Model.Data.Data;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
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
    public void showData(Data d) {
        DataHikiate data = (DataHikiate) d;

        if (!TextUtils.isEmpty(data.ErrMsg)) {
            kokban_text.setText("");
            return;
        }

        kokban_text.setText(data.KOKBAN);
        hinban_text.setText(data.SM21S_DTKSHIN);
        lotno_text.setText(data.MD01_LOTBAN);
        carbon_text.setText(data.MM03_ZAINMK);

        kokban_text.setFocusable(false);
    }
}
