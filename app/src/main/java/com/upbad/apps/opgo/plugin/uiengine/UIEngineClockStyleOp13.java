package com.upbad.apps.opgo.plugin.uiengine;

import static com.upbad.apps.opgo.util.LogUtil.log;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UIEngineClockStyleOp13 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        log("UIEngine hook() started");

        // AOD digital clock (seems not effective; OOS is likely to adapt the OpKeyguardOplusTextView in SystemUI)
//        XposedHelpers.findAndHookMethod("com.oplus.egview.widget.OpKeyguardOplusTextView", classLoader, "getTextWithOplusColor", java.lang.CharSequence.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) {
//                CharSequence text = (CharSequence) param.args[0];
//                SpannableString spannableString = new SpannableString(text.toString());
//                param.setResult(spannableString);
//            }
//        });

        // AOD and keyguard clocks (effective)
        XposedHelpers.findAndHookMethod("com.oplus.egview.widget.TimeView", classLoader, "setHourRedOne", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                param.setResult(false);
            }
        });

        log("UIEngine hook() ended");
    }
}
