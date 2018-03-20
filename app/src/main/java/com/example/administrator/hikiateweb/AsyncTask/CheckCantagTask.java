package com.example.administrator.hikiateweb.AsyncTask;

import com.example.administrator.hikiateweb.Display.CantagDisplay;
import com.example.administrator.hikiateweb.Display.Display;
import com.example.administrator.hikiateweb.Display.KokanInfoDisplay;
import com.example.administrator.hikiateweb.Model.Data.DataCantag;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.View.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2018/03/20.
 */

public class CheckCantagTask extends AbstractAsyncTask {
    private Display display;
    private MainActivity activity;

    public CheckCantagTask(String urlStr, String requestMethod, MainActivity activity) {
        super(urlStr, requestMethod);
        this.display = new CantagDisplay(activity);
        this.activity = activity;
    }

    @Override
    public void applyDataToScreen(String result) {
        //受信したJsonデータを加工して、画面に反映させる
        try {
            //缶タグデータをパース
            ObjectMapper mapper = new ObjectMapper();
            DataCantag dataCantag = mapper.readValue(result, DataCantag.class);

            //Dataを引っ張ってきて、缶タグデータをプラス
            DataHikiate d = activity.getDataHikiate();
            d.PC01_CANNO.add(dataCantag.PC01_CANNO);
            d.PC01_KOKBAN.add(dataCantag.PC01_KOKBAN);

            display.showData(d);
            activity.setDataHikiate(d);
        }
        catch (Exception ex) {

        }

    }
}
