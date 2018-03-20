package com.example.administrator.hikiateweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.hikiateweb.AsyncTask.GetKokanInfoTask;
import com.example.administrator.hikiateweb.Display.KokanInfoDisplay;

public class MainActivity extends AppCompatActivity {

    private KokanInfoDisplay kokanInfoDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kokanInfoDisplay = new KokanInfoDisplay(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                String urlStr = "http://10.1.1.61/WebAPI_Koyo_C/hikiate/get/574237";
                GetKokanInfoTask task = new GetKokanInfoTask(urlStr, "GET", kokanInfoDisplay);
                task.execute();
            }
        });
    }
}
