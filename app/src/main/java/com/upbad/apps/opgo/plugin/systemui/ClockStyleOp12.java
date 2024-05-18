package com.upbad.apps.opgo.plugin.systemui;

import static com.upbad.apps.opgo.util.LogUtil.log;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upbad.apps.opgo.plugin.IPlugin;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ClockStyleOp12 implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader classLoader) {
        try {
            log("OOS 12 SystemUI hook() started");
            // on OnePlus OxygenOS 12, the logic changed
            // notification clock
            Class<?> kClockExt = classLoader.loadClass("com.oplusos.systemui.ext.ClockExt");
            // target method: com.oplusos.systemui.ext.ClockExt->setTextWithRedOneStyleInternal(TextView, CharSequence)V
            XposedHelpers.findAndHookMethod(kClockExt, "setTextWithRedOneStyleInternal",
                    TextView.class, CharSequence.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            TextView textView = (TextView) param.args[0];
                            CharSequence text = (CharSequence) param.args[1];
                            textView.setText(text.toString().replace(":", "∶"));
                        }
                    });

            // keyguard digital clock
            // com.oplusos.keyguard.OpKeyguardOplusTextView->getTextWithOplusColor(CharSequence)SpannableString
            Class<?> kOpKeyguardOplusTextView = classLoader.loadClass("com.oplusos.keyguard.OpKeyguardOplusTextView");
            XposedHelpers.findAndHookMethod(kOpKeyguardOplusTextView, "getTextWithOplusColor",
                    CharSequence.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            CharSequence text = (CharSequence) param.args[0];
                            SpannableString spannableString = new SpannableString(text.toString());
                            param.setResult(spannableString);
                        }
                    });

            // on OOS 12, the AOD is a standalone app whose package name is "com.oplus.aod"
            // the ClassLoader of this app is not the same as the ClassLoader of the main app
            // entry point: com.oplusos.systemui.aod.plugin.AodForeignPackage->initForeignPackage()Z
            XposedHelpers.findAndHookMethod("com.oplusos.systemui.aod.plugin.AodForeignPackage", classLoader,
                    "initForeignPackage", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (Boolean.TRUE.equals(param.getResult())) {
                                Context foreignContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mForeignContext");
                                if (foreignContext != null && TextUtils.equals(foreignContext.getPackageName(), "com.oplus.aod")) {
                                    ClassLoader pluginClassLoader = (ClassLoader) XposedHelpers.getObjectField(param.thisObject, "mPluginClassLoader");
                                    if (pluginClassLoader != null) {
                                        hookAodPlugin(pluginClassLoader);
                                    }
                                }
                            }
                        }
                    });

            // the following 2 found by searching, but not sure if they are used in OOS 12
            Class<?> kRedTextClock = classLoader.loadClass("com.oplusos.systemui.keyguard.clock.RedTextClock");
            // super class: androidx.appcompat.widget.AppCompatTextView
            // target: RedTextClock->onTimeChanged()V
            XposedHelpers.findAndHookMethod(kRedTextClock, "onTimeChanged", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    TextView textView = (TextView) param.thisObject;
                    CharSequence text = textView.getText();
                    if (text instanceof SpannableStringBuilder) {
                        String rawText = text.toString();
                        // update the text
                        textView.setText(rawText);
                    }
                }
            });
            Class<?> kRedHorizontalDualClockView = classLoader.loadClass("com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView");
            // com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView$updateLocateTime$1$1
            // com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView$updateResidentTime$1$1
            // interface: java.lang.Runnable
            XC_MethodHook updateTimeHook = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object that1 = XposedHelpers.getObjectField(param.thisObject, "this$0");
                    Object that2 = XposedHelpers.getObjectField(that1, "this$0");
                    ViewGroup clockView = (ViewGroup) that2;
                    // check if we are updating the locate time or the resident time
                    String thisClassName = param.thisObject.getClass().getName();
                    // only hour has red color, minute does not
                    TextView hourTextView = null;
                    if (thisClassName.contains("updateLocateTime")) {
                        hourTextView = (TextView) XposedHelpers.getObjectField(clockView, "mTvHorizontalLocateClockHour");
                    } else if (thisClassName.contains("updateResidentTime")) {
                        hourTextView = (TextView) XposedHelpers.getObjectField(clockView, "mTvHorizontalResidentClockHour");
                    }
                    if (hourTextView != null) {
                        CharSequence text = hourTextView.getText();
                        if (text instanceof SpannableStringBuilder) {
                            String rawText = text.toString();
                            // update the text
                            hourTextView.setText(rawText);
                        }
                    }
                }
            };
            XposedHelpers.findAndHookMethod("com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView$updateLocateTime$1$1"
                    , classLoader, "run", updateTimeHook);
            XposedHelpers.findAndHookMethod("com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView$updateResidentTime$1$1"
                    , classLoader, "run", updateTimeHook);
            // end of OnePlus OxygenOS 12
            log("OOS 12 SystemUI hook() ended");
        } catch (ClassNotFoundException e) {
            log("ClockStyleOp12 hook error: " + e.getMessage());
        }
    }

    private static void hookAodPlugin(ClassLoader plugin) throws ClassNotFoundException {
        // AOD digital clock in AOD plugin
        // target: OpKeyguardOplusTextView->getTextWithOplusColor(CharSequence charSequence)SpannableString
        Class<?> kOpKeyguardOplusTextView = plugin.loadClass("com.oplus.egview.widget.OpKeyguardOplusTextView");
        XposedHelpers.findAndHookMethod(kOpKeyguardOplusTextView, "getTextWithOplusColor", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                String text = ((CharSequence) param.args[0]).toString();
                SpannableString spannableString = new SpannableString(text);
                param.setResult(spannableString);
            }
        });
    }
}
