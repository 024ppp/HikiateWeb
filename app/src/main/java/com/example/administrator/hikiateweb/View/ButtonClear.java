package com.example.administrator.hikiateweb.View;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.hikiateweb.Util.Util;

/**
 * Created by Administrator on 2018/04/12.
 */

public class ButtonClear extends AppCompatButton implements View.OnClickListener {
    public ButtonClear(Context context) {
        this(context, null);
    }

    public ButtonClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Util.pushClear();
    }

}
