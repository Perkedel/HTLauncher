@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)
@file:Suppress("PreviewApiLevelMustBeValid")

package com.perkedel.htlauncher.ui.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.Wallpapers
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.navigation.Configurationing
import com.perkedel.htlauncher.ui.navigation.HomeScreen
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Preview(
    name = "Handy Talky POC",
    group = "Special Embedded",
    device = "spec:width=350dp,height=280dp,dpi=1", apiLevel = 28, fontScale = .81f,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Handy Talky POC Actual",
    group = "Special Embedded",
    device = "spec:width=350dp,height=280dp,dpi=1", apiLevel = 21, fontScale = .81f,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class HTPreviewAnnotationsPOC

@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Red",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Blue",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Green",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Yellow",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
annotation class PreviewDynamicColorsWorkaroundLight

@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Red Dark",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Blue Dark",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Green Dark",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Yellow Dark",
    group = "Dynamic Color Workarounds",
    apiLevel = 31,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class PreviewDynamicColorsWorkaroundDark

@Preview(
    name = "WearOS", device = "id:wearos_large_round", showSystemUi = true, showBackground = true,
    group = "WearOS",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_WATCH
)
annotation class PreviewWearOS

@Preview(
    name = "WearOS Square", device = "id:wearos_square", showSystemUi = true, showBackground = true,
    group = "WearOS",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_WATCH
)
annotation class PreviewWearOsSquare

@Preview(
    name = "WearOS Rectangular", device = "id:wearos_rect", showSystemUi = true,
    group = "WearOS",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_WATCH
)
annotation class PreviewWearOsRectangular

@Preview(
    name = "Nexus 5",
    group = "Special Embedded",
    device = "spec:parent=Nexus 5,navigation=buttons", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class PreviewNexus5

@Preview(
    name = "Chromebook",
    group = "Android-x86",
    device = "id:desktop_large", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_DESK
)
annotation class PreviewChromebook

@Preview(
    name = "Pixel Fold", device = "id:pixel_9_pro_fold", showSystemUi = true, showBackground = true,
            group = "Special Embedded",
)
annotation class PreviewPixelFold

@Preview(
    name = "Microwave",
    group = "Special Embedded",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_APPLIANCE,
    device = "spec:width=1080px,height=2400px,orientation=landscape,cutout=corner,navigation=buttons"
)
annotation class PreviewMicrowave

@Preview(
    name = "Car Infotainment Portrait",
    group = "Special Embedded",
    device = "spec:parent=automotive_large_portrait", showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_CAR
)
annotation class PreviewCarPortrait

@Preview(
    name = "Car Infotainment Landscape", device = "id:automotive_1080p_landscape",
    group = "Special Embedded",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_CAR
)
annotation class PreviewCarLandscape

@Preview(
    name = "Car Distant", device = "id:automotive_distant_display_with_play",
    group = "Special Embedded",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_CAR
)
annotation class PreviewCarDistant

@Preview(
    name = "Google TV", device = "id:tv_4k",
    group = "Special Embedded",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_TELEVISION
)
annotation class PreviewGoogleTV

@Preview(
    name = "VR", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_VR_HEADSET,
    group = "Special Embedded",
    device = "spec:width=1920dp,height=1080dp,dpi=640,navigation=buttons", showSystemUi = true,
    showBackground = true
)
annotation class PreviewVR

@Preview(
    name = "Indonesian Portrait",
    group = "locale",
    locale = "id",
    device = "spec:width=1344px,height=2992px,dpi=480,cutout=punch_hole", showSystemUi = true, showBackground = true,
    backgroundColor = 0xFFFF9494,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
)
@Preview(
    name = "Indonesian Landscape",
    group = "locale",
    locale = "id",
    device = "spec:width=1344px,height=2992px,dpi=480,orientation=landscape,cutout=punch_hole", showSystemUi = true,
    showBackground = true, backgroundColor = 0xFFFF9494,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
)
annotation class PreviewLocales

@PreviewWearOS
@PreviewWearOsRectangular
@PreviewWearOsSquare
annotation class PreviewPocketWatches

@PreviewNexus5
@PreviewChromebook
@PreviewPixelFold
annotation class PreviewPhonesTablet

@PreviewMicrowave
@PreviewCarPortrait
@PreviewCarLandscape
@PreviewCarDistant
@PreviewGoogleTV
@PreviewVR
annotation class PreviewEmbedded

@HTPreviewAnnotationsPOC
@PreviewPocketWatches
@PreviewPhonesTablet
@PreviewEmbedded
annotation class PreviewDevices

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
annotation class PreviewScreens

@PreviewScreens
@PreviewDynamicColors
annotation class PreviewAndroidX

@Preview(showBackground = true)
@PreviewDevices
@PreviewDynamicColorsWorkaroundLight
@PreviewDynamicColorsWorkaroundDark
@PreviewAndroidX
@PreviewLocales
annotation class HTPreviewAnnotations

@PreviewDynamicColorsWorkaroundLight
@PreviewDynamicColorsWorkaroundDark
@PreviewDynamicColors
@Preview(showBackground = true)
annotation class PreviewButtons

@HTPreviewAnnotations
@Composable
fun ExampleHTPreviewAnnotations(){
    HTLauncherTheme {
        Surface {
            Scaffold(
                topBar = {
                    HTAppBar(
                        canNavigateBack = true,
                        onMoreMenu = {}
                    )
                },
                modifier = Modifier,

            )
            { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding),
                ){
                    HomeScreen()
//                    Configurationing()
                }
            }
        }

    }
}

//@PreviewWearOS
//@Composable
//fun ExampleHTPreviewWearAnnotations(){
//    // https://youtu.be/2t02rF1Tybg
//    // https://youtu.be/irIGZj1YON8
//    // https://github.com/stevdza-san
//    // https://github.com/philipplackner/WearOsStopWatch
//    // https://developer.samsung.com/watch-face-studio/overview.html
//    HTLauncherTheme {
//
//    }
//}