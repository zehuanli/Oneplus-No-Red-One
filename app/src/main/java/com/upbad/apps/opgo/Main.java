package com.upbad.apps.opgo;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import com.upbad.apps.opgo.plugin.systemui.ClockStyle;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp12;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp13;
import com.upbad.apps.opgo.plugin.IPlugin;
import com.upbad.apps.opgo.plugin.uiengine.UIEngineClockStyleOp13;
import com.upbad.apps.opgo.util.HookParams;

import static de.robv.android.xposed.XposedBridge.log;

public class Main implements IXposedHookLoadPackage {

    private static final IPlugin[] UIENGINE_PLUGINS =  {
            new UIEngineClockStyleOp13()
    };

    private static final IPlugin[] SYSTEMUI_PLUGINS = {
            android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.R ? new ClockStyle()
                    : (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.S ? new ClockStyleOp12() : new ClockStyleOp13())
    };

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        switch (lpparam.packageName) {
            case HookParams.UIENGINE_PACKAGE_NAME:
                if (! (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.S)) {
                    loadPlugins(UIENGINE_PLUGINS, lpparam, lpparam.classLoader);
                }
                break;
            case HookParams.SYSTEMUI_PACKAGE_NAME:
                loadPlugins(SYSTEMUI_PLUGINS, lpparam, lpparam.classLoader);
                break;
            default:
        }
    }

    private void loadPlugins(IPlugin[] plugins, LoadPackageParam lpparam, ClassLoader classLoader) {
        for (IPlugin plugin : plugins) {
            try {
                plugin.hook(lpparam, classLoader);
            } catch (Error | Exception e) {
                log("loadPlugins error: " + e);
            }
        }
    }
}
