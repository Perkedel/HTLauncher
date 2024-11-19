@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

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
    device = "spec:width=350dp,height=280dp,dpi=1", apiLevel = 31, fontScale = .81f,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Handy Talky POC Actual",
    device = "spec:width=350dp,height=280dp,dpi=1", apiLevel = 28, fontScale = .81f,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class HTPreviewAnnotationsPOC

@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color",
    apiLevel = 31,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Red",
    apiLevel = 31,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Blue",
    apiLevel = 31,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Green",
    apiLevel = 31,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Yellow",
    apiLevel = 31,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color",
    apiLevel = 31,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
annotation class PreviewDynamicColorsWorkaroundLight

@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Red Dark",
    apiLevel = 31,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Blue",
    apiLevel = 31,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Green",
    apiLevel = 31,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    // https://stackoverflow.com/a/78279389/9079640
    name = "Workaround Dynamic Color Yellow",
    apiLevel = 31,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class PreviewDynamicColorsWorkaroundDark

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
annotation class PreviewScreens

@PreviewScreens
@PreviewDynamicColors
annotation class PreviewAndroidX

@HTPreviewAnnotationsPOC
@PreviewDynamicColorsWorkaroundLight
@PreviewDynamicColorsWorkaroundDark
@PreviewAndroidX
@Preview(showBackground = true)
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
//                    HomeScreen()
                    Configurationing()
                }
            }
        }

    }
}