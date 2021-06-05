# Description
基于Xposed，去除一加系统锁屏和AOD时钟红色字体样式的模块。

An Xposed module to prevent Oneplus from making red "1" digits on the clocks.

If you hate the red "1" characters on the lock screen clock and AOD (Always-On Display) clock like me, you may try this Xposed module to get rid of it.

For the clock on the lock screen, it should work just fine. For the AOD clock, I only modified the "Digital 2". However, it should be fairly simple for other clocks.

# Some details
## The work was based on
- App and version: com.android.systemui, v30
- Test phone: Oneplus 8T
- Build number: Oxygen OS 11.0.8.11.KB05AA
- Android version: 11
- Tested on LSPosed but should work on other Xposed-like frameworks

## AOD Clock - Class Mapping
- "Default" - ?
- "Insight" - com.oneplus.aod.views.parsons.OpParsonsClock?
- "Text Clock" - com.oneplus.aod.views.OpTextClock?
- "Bold" - ?
- "Digital 1" - ?
- "Digital 2" - com.oneplus.aod.views.OpOneColorStyleClock
- "Digital 3" - com.oneplus.aod.OpBigDigitClock
- "Analog 1" - ?
- "Analog 2" - ?
- "Analog 3" - ?
- "Minimalism 1" - ?
- "Minimalism 2" - ?

## Other AOD clocks I found with the clock styles above
- com.oneplus.aod.views.bitmoji.OpBitmojiClock
- com.oneplus.aod.views.OpAnalogClock
- com.oneplus.aod.views.OpMin2AnalogClock
- com.oneplus.aod.views.OpTypoGraphicClock
- com.oneplus.aod.OpAnalogClock
- com.oneplus.aod.OpMimimalismClock
