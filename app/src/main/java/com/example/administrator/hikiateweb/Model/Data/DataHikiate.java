package com.example.administrator.hikiateweb.Model.Data;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/03/20.
 */

public class DataHikiate extends Data {
    //工管番号で取得する分
    public String KOKBAN = "";
    public String SM21S_DTKSHIN = "";
    public String MD01_LOTBAN = "";
    public String MD01_SFCD = "";
    public String MM03_CBNCOD = "";
    public String MM03_ZAINMK = "";

    //缶タグ情報（缶数分）
    public ArrayList<String> PC01_CANNO = new ArrayList<String>();
    public ArrayList<String> PC01_KOKBAN = new ArrayList<String>();
    public ArrayList<String> MD01X_LOTBAN = new ArrayList<String>();
}
