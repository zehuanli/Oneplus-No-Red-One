package com.upbad.apps.opgo.plugin.systemui;

import android.text.SpannableString;
import android.widget.TextView;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class ClockStyleOp13 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("OOS 13 SystemUI hook() started");

        // Status bar clock
        XposedHelpers.findAndHookMethod("com.oplusos.systemui.ext.BaseClockExt", classLoader, "setTextWithRedOneStyleInternal", android.widget.TextView.class, java.lang.CharSequence.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                TextView textView = (TextView) param.args[0];
                CharSequence text = (CharSequence) param.args[1];
                textView.setText(text.toString().replace(":", "\u200e∶"));
            }
        });

        // Keyguard clock
        XposedHelpers.findAndHookMethod("com.oplusos.keyguard.OpKeyguardOplusTextView", classLoader, "getTextWithOplusColor", java.lang.CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                CharSequence text = (CharSequence) param.args[0];
                SpannableString spannableString = new SpannableString(text.toString());
                param.setResult(spannableString);
            }
        });

        log("OOS 13 SystemUI hook() ended");
    }
}
