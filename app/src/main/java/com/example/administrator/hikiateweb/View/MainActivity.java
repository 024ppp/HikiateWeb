package com.example.administrator.hikiateweb.View;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.hikiateweb.Model.Data.DataHikiate;
import com.example.administrator.hikiateweb.R;
import com.example.administrator.hikiateweb.Controller.NfcTags;
import com.example.administrator.hikiateweb.Util.Constants;
import com.example.administrator.hikiateweb.Util.Util;

public class MainActivity extends AppCompatActivity {
    DataHikiate dataHikiate;
    NfcTags nfcTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Util使用準備
        Util.Set(this);

        nfcTags = new NfcTags(this);

        Util.showMessage(Constants.MSG_STR);
        //タイトルを動的に変更
        setTitle("引当て" + Util.getKojokbn(1));
        //サーバー通信チェック
        Util.confirmServerConnection();
    }

    //タグを読み込んだ時に実行される
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //バイブ
        Util.vibrate(Constants.VIB_NORMAL);
        //タグテキスト抽出
        String tag = this.nfcTags.getStringInTag(intent);

        if (!TextUtils.isEmpty(tag)) {
            //缶タグ追加処理
            Util.sendCannoAfterTouch(tag);
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
                //再起動
                reload();
                break;
            default:
                break;
        }
    }

    //再起動
    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
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
