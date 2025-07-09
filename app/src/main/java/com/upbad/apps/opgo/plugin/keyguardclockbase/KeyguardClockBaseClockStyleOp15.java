package com.upbad.apps.opgo.plugin.keyguardclockbase;

import android.text.SpannableStringBuilder;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class KeyguardClockBaseClockStyleOp15 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("OOS 15 KeyguardClockBase hook() started");

        XposedHelpers.findAndHookMethod("com.oplus.keyguard.clock.base.widget.CustomizedTextView", classLoader, "setHourText", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(null);
            }
        });

        log("OOS 15 KeyguardClockBase hook() ended");
    }
}
