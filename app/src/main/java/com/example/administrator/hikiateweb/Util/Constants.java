package com.example.administrator.hikiateweb.Util;

import android.app.admin.SystemUpdateInfo;

/**
 * Created by Administrator on 2018/03/27.
 */

public final class Constants {
    //String
    public static final String hoge = "";
    public static final String MSG_STR = "工管番号をスキャンしてください。";
    public static final String MSG_CAN_STR = "缶タグをタッチしてください。";
    public static final String MSG_CAN_NEXT = "次の缶タグをタッチするか、\n登録してください。";
    public static final String MSG_CAN_FIN = "全てＯＫです。\n登録してください。";
    public static final String MSG_CAN_MAX = "最大数スキャン済みです。\n登録してください。";
    public static final String MSG_CAN_SCANNED = "はスキャン済みです。";
    public static final String MSG_EMPTY = "レスポンスデータが空白です。";
    public static final String MSG_TIMEOUT = "接続がタイムアウトしました。\n再試行してください。";
    public static final String MSG_SERVER_ERR = "サーバーとの通信が行われていません。";

    public static final String URI_GET = "/WebAPI_Koyo_C/hikiate/get/";
    public static final String URI_CHECK = "/WebAPI_Koyo_C/hikiate/check/";
    public static final String URI_POST = "/WebAPI_Koyo_C/api/hikiate";
    public static final String URI_SERVER = "/WebAPI_Koyo_C/api/values";

    public static final String STR_TIMEOUT = "TIMEOUT";
    public static final String STR_SERVER_ERR = "SERVER_ERR";

    //long
    public static final long PATTERN_NORMAL[] = {0, 200};
    public static final long PATTERN_ERROR[] = {0, 500, 200, 500};

    //int
    public static final int TIMEOUT_MILLSEC = 5000;
    public static final int CNT_CAN_MAX = 6;
    public static final int SETTING = 8888;
    public static final int VIB_NORMAL = 0;
    public static final int VIB_ERROR = 1;

    private Constants (){}
}
