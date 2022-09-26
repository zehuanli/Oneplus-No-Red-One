package com.upbad.apps.opgo.util;


import android.util.Log;
import de.robv.android.xposed.XposedBridge;

public final class LogUtil {

    public static void log(String msg) {
        if (msg == null) {
            return;
        }
        try {
            XposedBridge.log("[OPGO] " + msg);
        } catch (Exception unused) {
            Log.i("OPGO", msg);
        }
    }

    public static void log(Throwable e) {
        if (e == null) {
            return;
        }
        log(Log.getStackTraceString(e));
    }

}
