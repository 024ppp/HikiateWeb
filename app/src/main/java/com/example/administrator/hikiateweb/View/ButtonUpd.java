package com.example.administrator.hikiateweb.View;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.hikiateweb.Util.HikiateUtil;

/**
 * Created by Administrator on 2018/04/12.
 */

public class ButtonUpd extends AppCompatButton implements View.OnClickListener {
    public ButtonUpd(Context context) {
        this(context, null);
    }

    public ButtonUpd(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        HikiateUtil.pushUpd();
    }

}
