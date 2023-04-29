*This module is archived as I'm no longer using OOS, after encountering a severe bug that forced me to factory reset.*

# Description
基于Xposed，去除一加系统锁屏和AOD时钟红色字体样式的模块。

An Xposed module to prevent Oneplus from making red "1" digits on the clocks.

If you hate the red "1" characters on the lock screen clock and AOD (Always-On Display) clock like me, you may try this Xposed module to get rid of it.

Supports OOS 11, OOS 12, and OOS 13.

## Issue with the OnePlus clock widget
On OOS 13, the OnePlus clock widget has a standalone package `com.oneplus.deskclock` that renders the red "1" digits. This package has been obfuscated in a way such that a fix would either be *temporary* (that stops working after an update of this widget) or *overkilling* (hooking an Android-wide function which introduces unnecessary computations to other unrelated codes). Thus, I would not fix it and just use some other clock widgets.

For those desired, there are two functions in the package `com.oneplus.deskclock` that use `SpannableStringBuilder` to set a red color for "1"s. Modify these two functions to stop this behavior. Alternatively, you may hook the function `setSpan` of `SpannableStringBuilder` to stop its execution whenever a character '1' is being modified. Again, this is either temporary or overkilling.
