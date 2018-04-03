package com.example.administrator.hikiateweb.AsyncTask;


import android.text.TextUtils;

import com.example.administrator.hikiateweb.Display.Display;
import com.example.administrator.hikiateweb.Display.KokanInfoDisplay;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.View.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2018/03/16.
 */

public class GetKokanInfoTask extends AbstractAsyncTask {
    private MainActivity activity;
    private Display display;

    public GetKokanInfoTask(MainActivity activity, String urlStr, String requestMethod) {
        super(activity, urlStr, requestMethod, true);
        this.activity = activity;
        this.display = new KokanInfoDisplay(activity);
    }

    @Override
    public void applyDataToScreen(String result) {
        //受信したJsonデータを加工して、画面に反映させる
        try {
            //なぜかサーバー側からレスポンスがずっと返ってくる...
            //が、↓のように条件分岐を入れたら解決した。謎である
            DataHikiate dataHikiate = activity.getDataHikiate();
            //レスポンスを受信済みであったら処理しない
            if (dataHikiate != null) {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            DataHikiate d = mapper.readValue(result, DataHikiate.class);
            //画面に反映する
            if (display.showData(d)) {
                //データを保持
                activity.setDataHikiate(d);
            }
        }
        catch (IOException ex) {

        }

    }

    @Override
    public void afterTimeoutProcess() {
        display.showTimeoutMessage();
    }
}
