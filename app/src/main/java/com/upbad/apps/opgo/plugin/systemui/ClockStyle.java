package com.upbad.apps.opgo.plugin.systemui;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.upbad.apps.opgo.plugin.IPlugin;

import java.util.Calendar;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.upbad.apps.opgo.util.LogUtil.log;

public class ClockStyle implements IPlugin {
    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam, final ClassLoader classLoader) {
        log("ClockStyle hook() started");
        // For AOD, clock digital 2
        // Do the same things as the original method, except changing the color of the hour digits
        // A better way might be just changing the field `mSpecialColor` to a normal (white) color, but I didn't find a proper color to borrow in this class
        XposedHelpers.findAndHookMethod("com.oneplus.aod.views.OpOneColorStyleClock", classLoader, "onTimeChanged", Calendar.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                try {
                    boolean is24HourModeEnabled = (boolean) XposedHelpers.callMethod(param.thisObject, "is24HourModeEnabled");
                    String charSequence = DateFormat.format(
                            is24HourModeEnabled ?
                                    (CharSequence) XposedHelpers.getObjectField(param.thisObject, "mFormat24")
                                    : (CharSequence) XposedHelpers.getObjectField(param.thisObject, "mFormat12"),
                            ((Calendar) param.args[0]).getTime()).toString();
                    SpannableString spannableString = new SpannableString(charSequence);
                    XposedHelpers.callMethod(param.thisObject, "setText", TextUtils.expandTemplate(spannableString));
                    // Prevent the call to the original method
                    param.setResult(null);
                } catch (Error | Exception e) {
                    log(e);
                }
            }
        });

        // For keyguard clock
        // Same as above, simple and efficient.
        XposedHelpers.findAndHookMethod("com.oneplus.keyguard.OpKeyguardOneplusTextView", classLoader, "getTextWithOneplusColor", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                try {
                    SpannableString spannableString;
                    if (param.args[0] == null) {
                        spannableString = new SpannableString("");
                    } else {
                        spannableString = new SpannableString((CharSequence) param.args[0]);
                    }
                    param.setResult(spannableString);
                } catch (Error | Exception e) {
                    log(e);
                }
            }
        });

        // For status bar clock
        // Same as above
        XposedHelpers.findAndHookMethod("com.oneplus.systemui.statusbar.policy.OpClock", classLoader, "setTextWithOpStyle", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                try {
                    String str = param.args[0].toString().replace(":", "᛬");
                    SpannableString spannableString = new SpannableString(str);
                    Object color = XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.oneplus.util.ThemeColorUtils", classLoader), "getColor", 1);
                    XposedHelpers.callMethod(param.thisObject, "setTextColor", color);
                    XposedHelpers.callMethod(param.thisObject, "setLayoutDirection", 0);
                    XposedHelpers.callMethod(param.thisObject, "setText", spannableString, TextView.BufferType.SPANNABLE);
                    param.setResult(null);
                } catch (Error | Exception e) {
                    log(e);
                }
            }
        });
        log("ClockStyle hook() finished");
    }
}
