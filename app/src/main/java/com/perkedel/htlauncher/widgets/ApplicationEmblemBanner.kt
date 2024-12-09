@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun ApplicationEmblemBanner(
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    modifierLogo: Modifier = Modifier,
    context:Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    pm: PackageManager = context.packageManager,
){
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {

                    }
                )
        ) {
            Column(
                modifier = Modifier
            ) {
                AsyncImage(
                    model = if(!LocalInspectionMode.current) pm.getApplicationIcon(context.packageName) else R.mipmap.ic_launcher,
                    modifier = modifierLogo
                        .size(100.dp)
//                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                    ,
                    placeholder = painterResource(R.drawable.mavrickle),
                    contentDescription = "a",
                )
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    modifier = modifierText.basicMarquee(),
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 38.sp
                )
                Text(
                    modifier = modifierText
                        .basicMarquee()
                    ,
                    text = context.packageName,
                    textAlign = TextAlign.Center,
                )
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
fun ApplicationEmblemBannerPreview(){
    HTLauncherTheme {
        Surface(
            color = rememberColorScheme().background
        ){
            ApplicationEmblemBanner()
        }
    }
}