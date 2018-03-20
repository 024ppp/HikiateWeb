package com.example.administrator.hikiateweb.View;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.hikiateweb.AsyncTask.GetKokanInfoTask;
import com.example.administrator.hikiateweb.Model.Data.Data;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.Controller.NfcTags;

public class MainActivity extends AppCompatActivity {

    String ip;

    DataHikiate dataHikiate;
    NfcTags nfcTags = null;


    //このへんの細々したもんはenumにまとめる
    Vibrator vib;
    final long m_vibPattern_read[] = {0, 200};
    final long m_vibPattern_error[] = {0, 500, 200, 500};
    static final int SETTING = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //接続先サーバのIPアドレスを取得
        SharedPreferences prefs = getSharedPreferences("ConnectionData", Context.MODE_PRIVATE);
        ip = "http://" + prefs.getString("ip", "");

        nfcTags = new NfcTags(this);
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);



        //test
        addTextChangedListener();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                String urlStr = "http://10.1.1.61/WebAPI_Koyo_C/hikiate/get/574237";
                GetKokanInfoTask task = new GetKokanInfoTask(urlStr, "GET", MainActivity.this);
                task.execute();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("title")
                        .setMessage(dataHikiate.KOKBAN + "  " + dataHikiate.ErrMsg)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    //test
    public void setDataHikiate(DataHikiate d) {
        this.dataHikiate = d;
    }
    public DataHikiate getDataHikiate() {
        return this.dataHikiate;
    }

    //タグを読み込んだ時に実行される
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //バイブ
        vib.vibrate(m_vibPattern_read, -1);

        String tagText = this.nfcTags.getStringInTag(intent);
        if (!TextUtils.isEmpty(tagText)) {
            //タグ読み取り時の処理
            String q = "?can=" + tagText + "&cbn=" + dataHikiate.MM03_CBNCOD;
            String urlStr = ip + "/WebAPI_Koyo_C/hikiate/check/" + q;
            GetKokanInfoTask task = new GetKokanInfoTask(urlStr, "GET", MainActivity.this);
            task.execute();
        }
    }

    //TODO URIをstring.xmlにおく。Util的なとこにまとめる
    //addTextChangedListener
    private void addTextChangedListener() {
        //バーコードリーダー対応
        final EditText e = findViewById(R.id.kokban_text);
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
                    String urlStr = ip + "/WebAPI_Koyo_C/hikiate/get/" + editText.getText().toString();
                    GetKokanInfoTask task = new GetKokanInfoTask(urlStr, "GET", MainActivity.this);
                    task.execute();
                    //キーボードをしまう
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    //メニュー関連
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //設定画面呼び出し
            Intent intent = new Intent(this, Setting.class);
            int requestCode = SETTING;
            startActivityForResult(intent, requestCode);
            return true;
        }
        else if (id == R.id.action_finish) {
            //Dialog(OK,Cancel Ver.)
            new AlertDialog.Builder(this)
                    .setTitle("確認")
                    .setMessage("終了してもよろしいですか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // OK button pressed
                            finishAndRemoveTask();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SETTING:
                Toast.makeText(this, "設定が完了しました。", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //終了処理群
    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = this.createPendingIntent();
        // Enable NFC adapter
        this.nfcTags.enable(this, pendingIntent);
    }
    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Disable NFC adapter
        this.nfcTags.disable(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.nfcTags = null;
    }
}
