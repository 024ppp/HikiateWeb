package com.example.administrator.hikiateweb.AsyncTask;


import com.example.administrator.hikiateweb.Display.Display;
import com.example.administrator.hikiateweb.Display.KokanInfoDisplay;
import com.example.administrator.hikiateweb.Model.Data.DataCantag;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.View.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2018/03/16.
 */

public class GetKokanInfoTask extends AbstractAsyncTask {
    private Display display;
    private MainActivity activity;

    public GetKokanInfoTask(String urlStr, String requestMethod, MainActivity activity) {
        super(urlStr, requestMethod);
        this.display = new KokanInfoDisplay(activity);
        this.activity = activity;
    }

    @Override
    public void applyDataToScreen(String result) {
        //受信したJsonデータを加工して、画面に反映させる
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataHikiate d = mapper.readValue(result, DataHikiate.class);
            display.showData(d);
            activity.setDataHikiate(d);
        }
        catch (IOException ex) {

        }

    }
}
