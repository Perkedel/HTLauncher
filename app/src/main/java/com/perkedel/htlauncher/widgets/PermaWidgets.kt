package com.perkedel.htlauncher.widgets

import android.content.res.Configuration
import android.os.Build
import android.os.SystemClock
import android.view.SoundEffectConstants
import android.view.View
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import java.text.SimpleDateFormat
import java.util.Date

//@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FirstPageCard(
    handoverText:String = "",
    isOnNumberWhat:Int = 0,
    modifier: Modifier,
    onMoreMenuButton: () -> Unit,
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    view: View = LocalView.current,
){
//    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    // https://www.geeksforgeeks.org/how-to-get-current-time-and-date-in-android-using-jetpack-compose/
//    val sdf = SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z")
    val sdf = SimpleDateFormat("yyyy-MM-dd '|' HH:mm:ss a z")
    val currentDateAndTime = sdf.format(Date())

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier
    ) {
        if(isCompact) {
            Row(
                modifier = Modifier
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = if (isCompact) Modifier
                        .weight(1f)
                    else Modifier
                        .fillMaxHeight()
                        .size(200.dp)

                ) {
                    Text("${handoverText} ${isOnNumberWhat} | ${currentDateAndTime}")

                    // https://www.geeksforgeeks.org/text-clock-in-android-using-jetpack-compose/
//                AndroidView(
//                    factory = { context ->
//                        TextClock(context).apply {
//                            // on below line we are setting 12 hour format.
////                            format12Hour?.let { this.format12Hour = "hh:mm:ss a" }
//                            format24Hour?.let { this.format24Hour = "hh:mm:ss a" }
//                            // on below line we are setting time zone.
//                            timeZone?.let { this.timeZone = it }
//                            // on below line we are setting text size.
////                            textSize.let { this.textSize = 30f }
//                            // pls format text
////                            textColors.let {this.textColors =  }
//                        }
//                    },
//                    modifier = Modifier.padding(5.dp),
//                )

                    // bottom the more menu?
                }
                IconButton(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onMoreMenuButton()
                    },
                    modifier = Modifier

                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu"
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = if (isCompact) Modifier
                    .weight(1f)
                else Modifier
                    .fillMaxHeight()
                    .size(200.dp)
            ) {
                Text("CLOCK")
                Text("BAT")
                Text("SIGNAL")
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                    ,
                )
                HTButton(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Menu",
                    leftIcon = Icons.Default.MoreVert,
                    buttonType = ButtonTypes.TextButton,
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onMoreMenuButton()
                    }
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
fun FirstPageCardPreview(){
    HTLauncherTheme {
        FirstPageCard(
            modifier = Modifier,
            onMoreMenuButton = {},

        )
    }
}