package com.example.administrator.hikiateweb.Display;

import android.widget.TextView;

import com.example.administrator.hikiateweb.Model.Data.Data;

/**
 * Created by Administrator on 2018/03/19.
 */

public abstract class Display {
    abstract public boolean showData(Data d);
    abstract public void showTimeoutMessage();
}
