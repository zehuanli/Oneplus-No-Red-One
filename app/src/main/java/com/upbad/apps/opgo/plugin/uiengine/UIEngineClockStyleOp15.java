package com.upbad.apps.opgo.plugin.uiengine;

import android.text.SpannableString;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class UIEngineClockStyleOp15 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("OOS 15 UIEngine hook() started");

        // AOD (classic)
        XposedHelpers.findAndHookMethod("com.oplus.egview.widget.TimeView", classLoader, "setHourRedOne", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(null);
            }
        });

        // Keyguard
        XposedHelpers.findAndHookMethod("com.oplus.egview.widget.OpKeyguardOplusTextView", classLoader, "getTextWithOplusColor", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(new SpannableString((String) param.args[0]));
            }
        });

        log("OOS 15 UIEngine hook() ended");
    }
}
