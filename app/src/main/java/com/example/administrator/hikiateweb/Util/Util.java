package com.example.administrator.hikiateweb.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.hikiateweb.AsyncTask.AbstractAsyncTask;
import com.example.administrator.hikiateweb.AsyncTask.CheckCantagTask;
import com.example.administrator.hikiateweb.AsyncTask.ConfirmServerTask;
import com.example.administrator.hikiateweb.AsyncTask.GetKokanInfoTask;
import com.example.administrator.hikiateweb.AsyncTask.UpdateProcessTask;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.View.MainActivity;
import java.util.concurrent.TimeUnit;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Administrator on 2018/03/27.
 */

//インスタンス化せず使用する感じになりそう
public class Util {
    private static MainActivity activity;
    private static TextView msg_text;
    private static String ip;
    private static Vibrator vib;

    //todo staticメソッドで使用しているメンバ変数が代入済みかどうかは未確認なので中々あぶない
    //todo そもそもstaticメソッドをここまで利用してていいんか？めっちゃ便利やけども
    //-----static-----
    //--public
    public static void Set(MainActivity mainActivity) {
        activity = mainActivity;
        msg_text = activity.findViewById(R.id.msg_text);

        //接続先サーバのIPアドレス(URI)を取得
        SharedPreferences prefs = activity.getSharedPreferences("ConnectionData", Context.MODE_PRIVATE);
        ip = "http://" + prefs.getString("ip", "");

        //バイブ設定
        vib = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
    }

    //起動時サーバーチェック
    public static void confirmServerConnection() {
        try {
            String urlStr = createURI("SERVER");
            ConfirmServerTask task = new ConfirmServerTask(activity, urlStr, "GET");
            task.execute();
        }
        catch (Exception ex) {
        }
    }

    //工管番号スキャン時本処理
    public static void sendKokban(EditText editText){
        String txt = editText.getText().toString();
        //文字数チェック
        if (txt.length() < 6) { return; }

        //工程管理Noが6文字以上になったら、工程管理番号問い合わせをサーバーに送信する
        String urlStr = createURI("GET") + txt;
        GetKokanInfoTask task = new GetKokanInfoTask(activity, urlStr, "GET");
        task.execute();
        //キーボードをしまう
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //todo NFCタグ値取得後にこっちが発生してしまうの腹立つなんとかせねば(未使用)
    //缶No.バーコードスキャン時本処理
    public static void sendCanno(EditText editText){
        String txt = editText.getText().toString();
        //文字数チェック
        if (txt.length() < 7) { return; }

        //クリア
        editText.setText("");

        //リクエスト送信可能かチェックしてから送信
        DataHikiate dataHikiate = activity.getDataHikiate();
        if (canSendRequest(dataHikiate, txt)) {
            //カーボンコード一致チェックを行う
            String q = "?can=" + txt + "&cbn=" + dataHikiate.MM03_CBNCOD;
            String urlStr = createURI("CHECK") + q;
            CheckCantagTask task = new CheckCantagTask(activity, urlStr, "GET");
            task.execute();
            //キーボードをしまう
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //缶タグタッチ時本処理
    public static void sendCannoAfterTouch(String tag) {
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

    //クリアボタン押下時本処理
    public static void pushClear() {
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

    //登録ボタン押下時本処理
    public static void pushUpd() {
        /* 20180711 ダイアログなしで更新
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
        */
        sendUpdateRequest();
    }

    //メッセージ表示
    public static void showMessage(String msg) {
        msg_text.setText(msg);
    }

    //工場区分取得
    public static String getKojokbn(int mode) {
        SharedPreferences prefs = activity.getSharedPreferences("ConnectionData", Context.MODE_PRIVATE);
        String kojokbn = prefs.getString("kojo", "");

        //0：工場区分をそのまま返す　1：工場名を返す
        switch (mode) {
            case 0:
                return kojokbn;
            case 1:
                switch (kojokbn) {
                    case "0":
                        return "(本社)";
                    case "1":
                        return "(広陽)";
                    case "2":
                        return "(玉城)";
                    default:
                        return "(工場区分未選択)";
                }
        }
        return "99";
    }

    //バイブ
    public static void vibrate(int mode) {
        switch (mode) {
            case Constants.VIB_NORMAL:
                //バイブ
                vib.vibrate(Constants.PATTERN_NORMAL, -1);
                break;
            case Constants.VIB_ERROR:
                //バイブエラー
                vib.vibrate(Constants.PATTERN_ERROR, -1);
                break;
        }
    }



    //--private
    //リクエスト先URI作成
    private static String createURI(String act) {
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
            case "SERVER":
                uri += Constants.URI_SERVER;
                break;
            default:
                uri = "";
                break;
        }
        return uri;
    }

    //リクエスト送信可能かチェック
    private static boolean canSendRequest(DataHikiate dataHikiate, String tag) {
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

    //更新リクエスト本処理
    private static void sendUpdateRequest() {
        DataHikiate dataHikiate = activity.getDataHikiate();
        //20180518 工場区分追加
        dataHikiate.KOJOKBN = getKojokbn(0);
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
    //-----------------

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

    //カスタムビューにリスナー処理を実装させた（？）ので不使用
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
}
