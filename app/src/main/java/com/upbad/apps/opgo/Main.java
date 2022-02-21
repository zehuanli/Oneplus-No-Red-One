package com.upbad.apps.opgo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import com.upbad.apps.opgo.plugin.ClockStyle;
import com.upbad.apps.opgo.plugin.ClockStyleOp12;
import com.upbad.apps.opgo.plugin.IPlugin;
import com.upbad.apps.opgo.util.HookParams;

import static de.robv.android.xposed.XposedBridge.log;

public class Main implements IXposedHookLoadPackage {

    private static final IPlugin[] plugins = {
            new ClockStyle(),
            new ClockStyleOp12()
    };

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(HookParams.SYSTEMUI_PACKAGE_NAME)) {
            return;

        }
        loadPlugins(lpparam, lpparam.classLoader);
    }

    private void loadPlugins(LoadPackageParam lpparam, ClassLoader classLoader) {
        for (IPlugin plugin : plugins) {
            try {
                plugin.hook(lpparam, classLoader);
            } catch (Error | Exception e) {
                log("loadPlugins error: " + e);
            }
        }
    }
}
