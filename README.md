<s>This module is archived as I'm no longer using OOS, after encountering a severe bug that forced me to factory reset.</s>
Well I had to switch back to OOS 14 for a while before a custom ROM is available.

# Description
基于Xposed，去除一加系统锁屏，AOD和状态栏时钟红色字体样式的模块。

An Xposed module to prevent Oneplus from making red "1" digits on the keyguard, AOD (Always-On Display), and status bar clocks.

If you hate the red "1" characters on the clocks like me, you may try this Xposed module to get rid of it.

Supports OOS 11, OOS 12, OOS 13, and OOS 14.

## Issue with the OnePlus clock widget
On OOS 13/14, the OnePlus clock widget has a standalone package `com.oneplus.deskclock` that renders the red "1" digits. This package has been obfuscated in a way such that a fix would either be *temporary* (that stops working after an update of this widget) or *overkilling* (hooking an Android-wide function which introduces unnecessary computations to other unrelated codes). Thus, I would not fix it and just use some other clock widgets.

For those desired, there are two functions in the package `com.oneplus.deskclock` that use `SpannableStringBuilder` to set a red color for "1"s. Modify these two functions to stop this behavior. Alternatively, you may hook the function `setSpan` of `SpannableStringBuilder` to stop its execution whenever a character '1' is being modified. Again, this is either temporary or overkilling.
