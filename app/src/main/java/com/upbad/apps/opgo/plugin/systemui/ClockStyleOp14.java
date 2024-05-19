package com.upbad.apps.opgo.plugin.systemui;

import android.text.SpannableStringBuilder;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class ClockStyleOp14 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("OOS 14 SystemUI hook() started");

        // Status bar clock
        XposedHelpers.findAndHookMethod("com.oplus.systemui.common.clock.OplusClockExImpl", classLoader, "setTextWithRedOneStyle", android.widget.TextView.class, java.lang.CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                XposedHelpers.callMethod(param.args[0], "setText", param.args[1]);
                param.setResult(XposedHelpers.getBooleanField(param.thisObject, "mIsDateTimePanel"));
            }
        });

        // Keyguard
        XposedHelpers.findAndHookMethod("com.oplus.keyguard.utils.KeyguardUtils", classLoader, "getSpannedHourString", android.content.Context.class, java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(new SpannableStringBuilder((String) param.args[1]));
            }
        });

        log("OOS 14 SystemUI hook() ended");
    }
}
