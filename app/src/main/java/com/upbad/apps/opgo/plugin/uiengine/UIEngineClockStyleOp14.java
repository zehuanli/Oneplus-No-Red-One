package com.upbad.apps.opgo.plugin.uiengine;

import android.text.SpannableString;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class UIEngineClockStyleOp14 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("OOS 14 UIEngine hook() started");

        // AOD
        XposedHelpers.findAndHookMethod("com.oplus.egview.widget.TimeView", classLoader, "setHourRedOne", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(null);
            }
        });

        log("OOS 14 UIEngine hook() ended");
    }
}
