package com.example.administrator.hikiateweb.View;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

import com.example.administrator.hikiateweb.AsyncTask.CheckCantagTask;
import com.example.administrator.hikiateweb.AsyncTask.GetKokanInfoTask;
import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.Controller.NfcTags;
import com.example.administrator.hikiateweb.Util.Constants;
import com.example.administrator.hikiateweb.Util.HikiateUtil;

public class MainActivity extends AppCompatActivity {
    DataHikiate dataHikiate;
    NfcTags nfcTags;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Util使用準備
        HikiateUtil.Set(this);

        nfcTags = new NfcTags(this);
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        HikiateUtil.showMessage(Constants.MSG_STR);
        //タイトルを動的に変更
        setTitle("引当て" + HikiateUtil.getKojokbn(1));
    }

    //タグを読み込んだ時に実行される
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //バイブ
        vib.vibrate(Constants.VIB_READ, -1);
        //タグテキスト抽出
        String tag = this.nfcTags.getStringInTag(intent);

        if (!TextUtils.isEmpty(tag)) {
            //缶タグ追加処理
            HikiateUtil.sendCannoAfterTouch(tag);
        }
    }

    //DataのGetter/Setter
    public void setDataHikiate(DataHikiate d) {
        this.dataHikiate = d;
    }
    public DataHikiate getDataHikiate() {
        return this.dataHikiate;
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
            int requestCode = Constants.SETTING;
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
            case Constants.SETTING:
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
