@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.startIntent
import com.perkedel.htlauncher.ui.navigation.AboutTerms
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

@Composable
fun AboutTermsOptions(
    modifier: Modifier = Modifier,
    navController: NavController? = rememberNavController(),
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    onReadTerms: () -> Unit = {},
    onReadDisclaimer: () -> Unit = {},
    addThese: @Composable ()->Unit = {},
){
    LazyColumn(
        modifier = modifier,
    ) {
        item { addThese() }
        preference(
            key = "versioning",
            title = { Text(text = "${stringResource(R.string.version_option)}: ${versionName}" ) },
            icon = { Icon(imageVector = Icons.Default.Circle, contentDescription = null) },
            summary = { Text(text = "${stringResource(R.string.version_code_option)}: ${versionNumber}. ${stringResource(R.string.click_here_to_check_update_snippet)}") },
            onClick = {

            }
        )
        preference(
            key = "android_versioning",
            title = { Text(text = "${stringResource(R.string.android_version_option)}: ${Build.VERSION.RELEASE}" ) },
            icon = { Icon(imageVector = Icons.Default.Android, contentDescription = null) },
            summary = { Text(text = "${stringResource(R.string.android_sdk_version_option)}: ${Build.VERSION.SDK_INT}. ${stringResource(R.string.click_here_to_check_update_snippet)}") },
            onClick = {

            }
        )
        preference(
            key = "source_code",
            title = { Text(text = stringResource(R.string.source_code_option)) },
            icon = { Icon(imageVector = Icons.Default.Source, contentDescription = null) },
            summary = { Text(text = "🦊 ${stringResource(R.string.source_code_site_0)}") },
            onClick = {
                // TODO: Open GitHub of this source code
                startIntent(context,
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            getString(context,
                                R.string.source_code_site_0)
                        ))
                )
            }
        )

        preference(
            key = "website",
            title = { Text(text = stringResource(R.string.web_site_option)) },
            icon = { Icon(imageVector = Icons.Default.Web, contentDescription = null) },
            summary = { Text(text = "🦊 ${stringResource(R.string.web_site_0)}") },
            onClick = {
                // TODO: Open Website
                // https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
                // https://stackoverflow.com/questions/3613722/can-one-combine-android-resource-strings-into-new-strings
                // https://stackoverflow.com/questions/10121802/android-reference-string-in-string-xml
                // https://developer.android.com/guide/topics/resources/string-resource
                startIntent(context,
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(context, R.string.web_site_0)))
                )
            }
        )

        preference(
            key = "terms",
            title = { Text(text = stringResource(R.string.terms_option)) },
            icon = { Icon(imageVector = Icons.Default.Source, contentDescription = null) },
            summary = { Text(text = "🦊 ${stringResource(R.string.source_code_site_0)}") },
            onClick = onReadTerms
        )

//            preference(
//                key = "disclaimer",
//                title = { Text(text = stringResource(R.string.terms_option)) },
//                icon = { Icon(imageVector = Icons.Default.Source, contentDescription = null) },
//                summary = { Text(text = "🦊 ${stringResource(R.string.source_code_site_0)}") },
//                onClick = onReadDisclaimer
//            )

        item{
            // Disclaimer Card! Expandable
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
                    .combinedClickable(
                        onClick = {

                        },
                        onLongClick = {

                        }
                    )
            ) {
                Text(text = stringResource(R.string.disclaimer_full))
            }
        }
    }
}

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@Preview(showBackground = true)
@Composable
fun AboutTermsOptionsPreview(){
    HTLauncherTheme {
        Surface(
            color = rememberColorScheme().background
        ) {
            ProvidePreferenceLocals {
                AboutTermsOptions()
            }

        }

    }
}