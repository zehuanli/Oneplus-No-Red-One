package com.upbad.apps.opgo;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.upbad.apps.opgo.plugin.IPlugin;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp11;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp12;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp13;
import com.upbad.apps.opgo.plugin.systemui.ClockStyleOp14;
import com.upbad.apps.opgo.plugin.uiengine.UIEngineClockStyleOp13;
import com.upbad.apps.opgo.plugin.uiengine.UIEngineClockStyleOp14;
import com.upbad.apps.opgo.util.HookParams;

import static de.robv.android.xposed.XposedBridge.log;

public class Main implements IXposedHookLoadPackage {

    private static final IPlugin[] UIENGINE_PLUGINS;

    private static final IPlugin[] SYSTEMUI_PLUGINS;

    static {
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.R:
                UIENGINE_PLUGINS = new IPlugin[] {};
                SYSTEMUI_PLUGINS = new IPlugin[] { new ClockStyleOp11() };
                break;
            case Build.VERSION_CODES.S:
            case Build.VERSION_CODES.S_V2:
                UIENGINE_PLUGINS = new IPlugin[] {};
                SYSTEMUI_PLUGINS = new IPlugin[] { new ClockStyleOp12() };
                break;
            case Build.VERSION_CODES.TIRAMISU:
                UIENGINE_PLUGINS = new IPlugin[] { new UIEngineClockStyleOp13() };
                SYSTEMUI_PLUGINS = new IPlugin[] { new ClockStyleOp13() };
                break;
            case Build.VERSION_CODES.UPSIDE_DOWN_CAKE:
                UIENGINE_PLUGINS = new IPlugin[] { new UIEngineClockStyleOp14() };
                SYSTEMUI_PLUGINS = new IPlugin[] { new ClockStyleOp14() };
                break;
            default:
                // Not supported
                UIENGINE_PLUGINS = new IPlugin[] {};
                SYSTEMUI_PLUGINS = new IPlugin[] {};
        }
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        switch (lpparam.packageName) {
            case HookParams.UIENGINE_PACKAGE_NAME:
                loadPlugins(UIENGINE_PLUGINS, lpparam, lpparam.classLoader);
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
