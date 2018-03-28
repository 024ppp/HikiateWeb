package com.example.administrator.hikiateweb.Display;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hikiateweb.Model.Data.Data;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.Util.Constants;

/**
 * Created by Administrator on 2018/03/13.
 */

public class KokanInfoDisplay extends Display{
    private EditText kokban_text, hinban_text, lotno_text, carbon_text;
    private TextView msg_text;

    public KokanInfoDisplay(Activity activity) {
        this.kokban_text = activity.findViewById(R.id.kokban_text);
        this.hinban_text = activity.findViewById(R.id.hinban_text);
        this.lotno_text = activity.findViewById(R.id.lotno_text);
        this.carbon_text = activity.findViewById(R.id.carbon_text);
        //msg
        this.msg_text = activity.findViewById(R.id.msg_text);
    }

    @Override
    public boolean showData(Data d) {
        DataHikiate data = (DataHikiate) d;

        if (!TextUtils.isEmpty(data.ErrMsg)) {
            kokban_text.setText("");
            msg_text.setText(data.ErrMsg);
            return false;
        }

        kokban_text.setText(data.KOKBAN);
        hinban_text.setText(data.SM21S_DTKSHIN);
        lotno_text.setText(data.MD01_LOTBAN);
        carbon_text.setText(data.MM03_ZAINMK);
        msg_text.setText(Constants.MSG_CAN_STR);

        kokban_text.setFocusable(false);
        return true;
    }

    @Override
    public void showTimeoutMessage() {
        kokban_text.setText("");
        msg_text.setText(Constants.MSG_TIMEOUT);
    }
}
