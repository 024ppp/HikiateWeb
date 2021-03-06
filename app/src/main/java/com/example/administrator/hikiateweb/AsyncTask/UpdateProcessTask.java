package com.example.administrator.hikiateweb.AsyncTask;

import com.example.administrator.hikiateweb.Util.Init;
import com.example.administrator.hikiateweb.View.MainActivity;

/**
 * Created by Administrator on 2018/03/27.
 */

public class UpdateProcessTask extends AbstractAsyncTask {
    private MainActivity activity;

    public UpdateProcessTask(MainActivity activity, String urlStr, String requestMethod) {
        super(activity, urlStr, requestMethod, true);
        this.activity = activity;
    }

    @Override
    public void applyDataToScreen(String result) {
        Init.initPage(activity);
    }

    @Override
    public void afterTimeoutProcess() {

    }
}
