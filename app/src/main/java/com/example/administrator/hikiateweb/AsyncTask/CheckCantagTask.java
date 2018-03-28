package com.example.administrator.hikiateweb.AsyncTask;

import android.text.TextUtils;

import com.example.administrator.hikiateweb.Display.CantagDisplay;
import com.example.administrator.hikiateweb.Display.Display;
import com.example.administrator.hikiateweb.Model.Data.DataCantag;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.Util.HikiateUtil;
import com.example.administrator.hikiateweb.View.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2018/03/20.
 */

public class CheckCantagTask extends AbstractAsyncTask {
    private MainActivity activity;
    private Display display;
    private HikiateUtil hikiateUtil;

    public CheckCantagTask(MainActivity activity, String urlStr, String requestMethod) {
        super(activity, urlStr, requestMethod);
        this.activity = activity;
        this.display = new CantagDisplay(activity);
        this.hikiateUtil = new HikiateUtil(activity);
    }

    @Override
    public void applyDataToScreen(String result) {
        //受信したJsonデータを加工して、画面に反映させる
        try {
            //缶タグデータをパース
            ObjectMapper mapper = new ObjectMapper();
            DataCantag dataCantag = mapper.readValue(result, DataCantag.class);

            //エラーチェック
            if (hikiateUtil.isErrorOccurred(dataCantag)) {
                return;
            }

            //Dataを引っ張ってきて、缶タグデータをプラス
            DataHikiate d = activity.getDataHikiate();
            d.PC01_CANNO.add(dataCantag.PC01_CANNO);
            d.PC01_KOKBAN.add(dataCantag.PC01_KOKBAN);
            d.MD01X_LOTBAN.add(dataCantag.MD01X_LOTBAN);

            //画面に反映する
            if (display.showData(d)) {
                //データを保持
                activity.setDataHikiate(d);
            }
        }
        catch (Exception ex) {

        }
    }

    @Override
    public void afterTimeoutProcess() {
        display.showTimeoutMessage();
    }
}
