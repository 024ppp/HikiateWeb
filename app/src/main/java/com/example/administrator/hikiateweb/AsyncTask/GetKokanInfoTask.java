package com.example.administrator.hikiateweb.AsyncTask;


import com.example.administrator.hikiateweb.Display.Display;

/**
 * Created by Administrator on 2018/03/16.
 */

public class GetKokanInfoTask extends AbstractAsyncTask {
    private Display display;

    public GetKokanInfoTask(String urlStr, String requestMethod, Display display) {
        super(urlStr, requestMethod);
        this.display = display;
    }

    @Override
    public void applyDataToScreen(String result) {
        display.setMsg("1","2", result, "4");
    }
}
