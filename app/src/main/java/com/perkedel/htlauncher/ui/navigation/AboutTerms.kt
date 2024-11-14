package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.startIntent
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

@Composable
fun AboutTerms(
    navController: NavController? = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    colorScheme: ColorScheme = rememberColorScheme(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSnackbarResult:(SnackbarResult) -> Unit = {  },
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    onReadTerms: () -> Unit = {},
    onReadDisclaimer: () -> Unit = {},
){
    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier
        )
        {
            // TODO: Big logo of this
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
                                painter = painterResource(R.drawable.mavrickle),
                                contentDescription = "a",
                            )
                            Spacer(
                                modifier = Modifier.fillMaxWidth().size(16.dp)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.app_name),
                                textAlign = TextAlign.Center,
                                fontSize = 38.sp
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = context.packageName,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                }
            }
            preference(
                key = "versioning",
                title = { Text(text = "${stringResource(R.string.version_option)}: ${versionName}" ) },
                icon = { Icon(imageVector = Icons.Default.Circle, contentDescription = null) },
                summary = { Text(text = "${stringResource(R.string.version_code_option)}: ${versionNumber}. Click here to check update") },
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
                    startIntent(context,Intent(Intent.ACTION_VIEW, Uri.parse(getString(context,R.string.source_code_site_0))))
                }
            )

            preference(
                key = "website",
                title = { Text(text = stringResource(R.string.source_code_option)) },
                icon = { Icon(imageVector = Icons.Default.Web, contentDescription = null) },
                summary = { Text(text = "🦊 ${stringResource(R.string.web_site_0)}") },
                onClick = {
                    // TODO: Open Website
                    // https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
                    // https://stackoverflow.com/questions/3613722/can-one-combine-android-resource-strings-into-new-strings
                    // https://stackoverflow.com/questions/10121802/android-reference-string-in-string-xml
                    // https://developer.android.com/guide/topics/resources/string-resource
                    startIntent(context,Intent(Intent.ACTION_VIEW, Uri.parse(getString(context,R.string.web_site_0))))
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutTermsPreview(){
    HTLauncherTheme {
        AboutTerms()
    }
}