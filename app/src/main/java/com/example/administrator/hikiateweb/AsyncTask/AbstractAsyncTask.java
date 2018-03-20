package com.example.administrator.hikiateweb.AsyncTask;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/03/16.
 */

public abstract class AbstractAsyncTask extends AsyncTask<String, String, String> {
    private String urlStr;
    private String requestMethod;

    public AbstractAsyncTask(String urlStr, String requestMethod) {
        this.urlStr = urlStr;
        this.requestMethod = requestMethod;
    }

    public abstract void applyDataToScreen(String result);

    @Override
    public String doInBackground(String... params) {
        HttpURLConnection con = null;
        InputStream is = null;
        String result = "";

        try {
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
            con.connect();

            is = con.getInputStream();
            result = convertResponseToString(is);
        }
        catch (Exception ex) {
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
            if(is != null) {
                try {
                    is.close();
                }
                catch(IOException ex) {
                }
            }
        }
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        applyDataToScreen(result);
    }

    // レスポンスデータをStringデータに変換する
    private String convertResponseToString(InputStream is) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while((st = br.readLine()) != null) {
            sb.append(st);
        }

        try {
            is.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
