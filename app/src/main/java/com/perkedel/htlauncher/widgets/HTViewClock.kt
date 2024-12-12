@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.perkedel.htlauncher.widgets

import android.graphics.Typeface as LegacyTypeface
import android.icu.text.DateFormat
import android.os.Build
import android.widget.TextClock
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import java.util.Calendar

@Composable
fun HTViewClock(
    modifier: Modifier = Modifier,
    showDate:Boolean = false,
    showSecond:Boolean = false,
    format: String = "kk:mm:ss",
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.labelLarge,
){
    // https://www.geeksforgeeks.org/text-clock-in-android-using-jetpack-compose/
    // https://stackoverflow.com/questions/68025651/whats-the-equivalent-of-textclock-in-jetpack-compose
    // https://www.droidcon.com/2023/06/30/crafting-a-clock-in-jetpack-compose-with-canvas/ wroong
    // https://github.com/mahdizareeii/Analog-Clock-Jetpack-Compose
    // https://www.youtube.com/watch?v=07ZdBCyh7sc
    // https://github.com/philipplackner/AnimatedCounterCompose
    // https://composables.com/wear-compose-material/timetext
    // https://www.geeksforgeeks.org/text-clock-in-android-using-jetpack-compose/
    // https://youtu.be/TeTKMQA9KzM https://youtu.be/bn_lL1H3njk Codex Creator datetime
    // https://stackoverflow.com/a/76163873/9079640
    val calendar = Calendar.getInstance()
    val time = calendar.time
    val dateFormat = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        DateFormat.getDateInstance(DateFormat.FULL).format(time)
    } else {
        time
    }
    val timeFormat = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
    } else {
        ""
    }

    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }
    val resolver = LocalFontFamilyResolver.current
    val face: LegacyTypeface = remember(resolver, style) {
        resolver.resolve(
            fontFamily = style.fontFamily,
            fontWeight = style.fontWeight ?: FontWeight.Normal,
            fontStyle = style.fontStyle ?: FontStyle.Normal,
            fontSynthesis = style.fontSynthesis ?: FontSynthesis.All
        )
    }.value as LegacyTypeface

//    AndroidView(
//        factory = { context ->
//            TextClock(context).apply {
//                // on below line we are setting 12 hour format.
////                            format12Hour?.let { this.format12Hour = "hh:mm:ss a" }
//                format24Hour?.let { this.format24Hour = "hh:mm:ss a" }
//                // on below line we are setting time zone.
//                timeZone?.let { this.timeZone = it }
//                // on below line we are setting text size.
////                            textSize.let { this.textSize = 30f }
//                // pls format text
////                            textColors.let {this.textColors =  }
//
//            }
//        },
//        modifier = Modifier.padding(5.dp),
//    )

    // Snoopy version https://stackoverflow.com/a/76163873/9079640
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextClock(context).apply {
                format24Hour?.let {
                    this.format24Hour = format
                }

                format12Hour?.let {
                    this.format12Hour = format
                }

                timeZone?.let { this.timeZone = it }
                textSize.let { this.textSize = style.fontSize.value }

                setTextColor(textColor.toArgb())

                typeface = face
            }
        }
    )

    val sayTotal:String = "${if(showDate) "$dateFormat, " else ""}$timeFormat"
//    OutlinedText(
//        text = sayTotal,
//        fillColor = rememberColorScheme().onSurface,
//        outlineColor = rememberColorScheme().surface,
//        modifier = modifier,
//        outlineDrawStyle = Stroke(
//            width = 2f,
//            miter = 5f,
//            join = StrokeJoin.Round,
//        ),
//    )
}

@HTPreviewAnnotations
@Composable
fun HTViewClockPreview(){
    HTLauncherTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                HTAppBar()
            }
        ) { padding ->
            Box(
                modifier = Modifier.padding(padding),
                contentAlignment = Alignment.Center
            ){
                Card(
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                    ){
                        Row {
                            HTViewClock(
                                modifier = Modifier
                            )
                        }

                    }

                }

            }
        }
    }
}