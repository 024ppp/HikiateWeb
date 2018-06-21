package com.example.administrator.hikiateweb.View;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.example.administrator.hikiateweb.Util.Util;

/**
 * Created by Administrator on 2018/04/12.
 */

public class EditTextKokban extends android.support.v7.widget.AppCompatEditText implements TextWatcher {
    public EditTextKokban(Context context) {
        this(context, null);
    }

    public EditTextKokban(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
    }
    @Override
    public void afterTextChanged(Editable s) {
        Util.sendKokban(this);
    }
}
