package com.example.administrator.hikiateweb.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hikiateweb.AsyncTask.AbstractAsyncTask;
import com.example.administrator.hikiateweb.AsyncTask.CheckCantagTask;
import com.example.administrator.hikiateweb.AsyncTask.GetKokanInfoTask;
import com.example.administrator.hikiateweb.AsyncTask.UpdateProcessTask;
import com.example.administrator.hikiateweb.Model.Data.Data;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.View.MainActivity;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2018/03/27.
 */

public class HikiateUtil {
    private MainActivity activity;
    private TextView msg_text;
    private String ip;

    public HikiateUtil(MainActivity activity) {
        this.activity = activity;
        this.msg_text = activity.findViewById(R.id.msg_text);

        //接続先サーバのIPアドレス(URI)を取得
        SharedPreferences prefs = activity.getSharedPreferences("ConnectionData", Context.MODE_PRIVATE);
        ip = "http://" + prefs.getString("ip", "");
    }

    //サーバーでエラーが起こったかどうかをチェック
    public boolean isErrorOccurred(Data d) {
        if (TextUtils.isEmpty(d.ErrMsg)) {
            return false;
        }

        if (this.msg_text == null) {
            return false;
        }

        msg_text.setText(d.ErrMsg);
        return true;
    }

    //メッセージ表示
    public void showMessage(String msg) {
        msg_text.setText(msg);
    }

    //リクエスト先URI作成
    public String createURI(String act) {
        String uri = ip;

        switch (act) {
            case "GET":
                uri += Constants.URI_GET;
                break;
            case "CHECK":
                uri += Constants.URI_CHECK;
                break;
            case "POST":
                uri += Constants.URI_POST;
                break;
            default:
                uri = "";
                break;
        }
        return uri;
    }

    //Listener追加
    public void addListener() {
        //---EditText---
        //バーコードリーダー対応
        final EditText e = activity.findViewById(R.id.kokban_text);
        e.addTextChangedListener(new TextWatcher(){
            EditText editText = e;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() >= 6) {
                    //工程管理Noが6文字以上になったら、工程管理番号問い合わせをサーバーに送信する
                    String urlStr = createURI("GET") + editText.getText().toString();
                    GetKokanInfoTask task = new GetKokanInfoTask(activity, urlStr, "GET");
                    task.execute();
                    //キーボードをしまう
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        //---Button---
        //Clear
        activity.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog(OK,Cancel Ver.)
                new android.app.AlertDialog.Builder(activity)
                        .setTitle("確認")
                        .setMessage("クリアしてよろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // OK button pressed
                                Init.initPage(activity);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        //Upd
        activity.findViewById(R.id.upd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog(OK,Cancel Ver.)
                new android.app.AlertDialog.Builder(activity)
                        .setTitle("確認")
                        .setMessage("登録しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // OK button pressed
                                sendUpdateRequest();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    //更新リクエスト本処理
    private void sendUpdateRequest() {
        DataHikiate dataHikiate = activity.getDataHikiate();
        try {
            String urlStr = createURI("POST");
            UpdateProcessTask task = new UpdateProcessTask(activity, urlStr, "POST");
            //送信データ作成
            String json = JsonConverter.toString(dataHikiate);
            task.execute(json);
        }
        catch (Exception ex) {
        }
    }

    //缶タグスキャン時本処理
    public void sendCheckCanTagRequest(String tag) {
        DataHikiate dataHikiate = activity.getDataHikiate();
        //リクエスト送信可能かチェックしてから送信
        if (canSendRequest(dataHikiate, tag)) {
            //カーボンコード一致チェックを行う
            String q = "?can=" + tag + "&cbn=" + dataHikiate.MM03_CBNCOD;
            String urlStr = createURI("CHECK") + q;
            CheckCantagTask task = new CheckCantagTask(activity, urlStr, "GET");
            task.execute();
        }
    }

    //リクエスト送信可能かチェック
    private boolean canSendRequest(DataHikiate dataHikiate, String tag) {
        //工管番号リード以降でないと処理できない
        if (dataHikiate == null) {
            return false;
        }

        //最大数スキャン済みかどうかチェック
        if (dataHikiate.isCanTagMaxCount()) {
            showMessage(Constants.MSG_CAN_MAX);
            return false;
        }

        //缶タグスキャン済みチェック
        if (dataHikiate.isThisCanTagScanned(tag)) {
            //スキャン済み
            showMessage(tag + Constants.MSG_CAN_SCANNED);
            return false;
        }
        return true;
    }

    //キャンセル後にProgressDialogが消えないため、不使用
    //APIリクエストのTimeout設定
    private void setTimeout(final AbstractAsyncTask task) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    task.get(1000, TimeUnit.MICROSECONDS);
                }
                catch (Exception ex) {
                    task.cancel(true);
                    msg_text.post(new Runnable() {
                        @Override
                        public void run() {
                            msg_text.setText(Constants.MSG_TIMEOUT);
                        }
                    });
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
