package com.example.administrator.hikiateweb.Display;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hikiateweb.Model.Data.Data;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.Util.Constants;

/**
 * Created by Administrator on 2018/03/20.
 */

public class CantagDisplay extends Display{
    private TextView[] arr_c_text;
    private TextView[] arr_l_text;
    private TextView msg_text;
    private Button upd_btn;

    public CantagDisplay(Activity activity) {
        //Cantag
        arr_c_text = new TextView[]{activity.findViewById(R.id.c1)
                                  , activity.findViewById(R.id.c2)
                                  , activity.findViewById(R.id.c3)
                                  , activity.findViewById(R.id.c4)
                                  , activity.findViewById(R.id.c5)
                                  , activity.findViewById(R.id.c6)};
        //Lotno
        arr_l_text = new TextView[]{activity.findViewById(R.id.l1)
                                  , activity.findViewById(R.id.l2)
                                  , activity.findViewById(R.id.l3)
                                  , activity.findViewById(R.id.l4)
                                  , activity.findViewById(R.id.l5)
                                  , activity.findViewById(R.id.l6)};
        //msg
        this.msg_text = activity.findViewById(R.id.msg_text);
        //btn
        this.upd_btn = activity.findViewById(R.id.upd_btn);
    }

    @Override
    public boolean showData(Data d) {
        DataHikiate data = (DataHikiate) d;

        if (!TextUtils.isEmpty(data.ErrMsg)) {
            msg_text.setText(data.ErrMsg);
            return false;
        }

        for (int i = 0; i < data.PC01_CANNO.size(); i++) {
            if (TextUtils.isEmpty(arr_l_text[i].getText())) {
                arr_c_text[i].setText(data.PC01_CANNO.get(i));
//                arr_c_text[i].setFocusable(false);
                arr_l_text[i].setText(data.MD01X_LOTBAN.get(i));
                break;
            }
        }

        //btn
        upd_btn.setEnabled(true);

        //next msg
        if (data.isCanTagMaxCount()) {
            msg_text.setText(Constants.MSG_CAN_FIN);
        }
        else {
            msg_text.setText(Constants.MSG_CAN_NEXT);
        }
        return true;
    }

    @Override
    public void showTimeoutMessage() {
        msg_text.setText(Constants.MSG_TIMEOUT);
    }
}