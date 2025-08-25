@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.graphics.Typeface as LegacyTypeface
import android.icu.text.DateFormat
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.TextClock
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun HTViewClock(
    modifier: Modifier = Modifier,
    minimalized:Boolean = false,
    context: Context = LocalContext.current,
    showDate:Boolean = false,
    showSecond:Boolean = false,
    format: String = "kk:mm:ss",
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onClick: ()->Unit = {},
    onLongClick: ()->Unit= {},
    haptic: HapticFeedback = LocalHapticFeedback.current,
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
    fun currentTime(): Time{
        // https://zsmb.co/compose-o-clock/
        // https://github.com/zsmb13/ComposeClock/blob/main/app/src/main/java/co/zsmb/composeclock/MainActivity.kt
        val cal = Calendar.getInstance()
        return Time(
            hoursTwelve = cal.get(Calendar.HOUR),
            hours = cal.get(Calendar.HOUR_OF_DAY),
            minutes = cal.get(Calendar.MINUTE),
            seconds = cal.get(Calendar.SECOND),
            amPm = cal.get(Calendar.AM_PM), // AM = 0, PM = 1
            // Why not also date? yeah!
            weekDay = cal.get(Calendar.DAY_OF_WEEK), // Sunday = 1, etc.
            day = cal.get(Calendar.DAY_OF_MONTH),
            month = cal.get(Calendar.MONTH),
            year = cal.get(Calendar.YEAR),
            // finally
            instance = cal
        )
    }
//    fun currentDate(): Date{
//        // Why not also date? yeah!
//        val cal = Calendar.getInstance()
//        return Date(
//            weekDay = cal.get(Calendar.DAY_OF_WEEK), // Sunday = 1, etc.
//            day = cal.get(Calendar.DAY_OF_MONTH),
//            month = cal.get(Calendar.MONTH),
//            year = cal.get(Calendar.YEAR),
//        )
//    }
    var clockTime by remember{ mutableStateOf(currentTime()) }
//    var clockDate by remember { mutableStateOf(currentDate()) }

    var time by remember { mutableStateOf(currentTime().instance.time) }
    var dateFormat:String by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DateFormat.getDateInstance(DateFormat.FULL).format(time)
            } else {
                fuzzyLegacyDate(clockTime,context)
            }
        )
    }
    var timeFormat:String by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
            } else {
                fuzzyLegacyTime(clockTime,context)
            }
        )
    }
    LaunchedEffect(0) {
        coroutineScope.launch {
            while (true) {
//            calendar.timeInMillis = System.currentTimeMillis()
//                Log.d("ClockTime", "Clock ${clockTime} ${clockDate}")
                clockTime = currentTime()
                time = currentTime().instance.time
//                clockDate = currentDate()
                dateFormat = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    DateFormat.getDateInstance(DateFormat.FULL).format(time)
                } else {
                    fuzzyLegacyDate(clockTime,context)
                }
                timeFormat = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
                } else {
                    fuzzyLegacyTime(clockTime,context)
                }

                delay(1000) // to prevent too many update a tick. maybe update just every second a.k.a. 1000 ms?
            }
        }
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
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            TextClock(context).apply {
//                format24Hour?.let {
//                    this.format24Hour = format
//                }
//
//                format12Hour?.let {
//                    this.format12Hour = format
//                }
//
//                timeZone?.let { this.timeZone = it }
//                textSize.let { this.textSize = style.fontSize.value }
//
//                setTextColor(textColor.toArgb())
//
//                typeface = face
//            }
//        }
//    )

    val compileTime:String = "${clockTime.hours}:${clockTime.minutes}:${clockTime.seconds}"
    val compileDate:String = "${clockTime.year}-${clockTime.month}-${clockTime.day}"
    val sayTotal:String = timeFormat
//    var sayTotal:String = "${if(showDate) "${clockTime.year}-${clockTime.month}-${clockTime.day}, " else ""}${clockTime.hours}.${clockTime.minutes}:${clockTime.seconds}"
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
    if(minimalized)
    {
        Row {
            //date
            if(showDate) {
            SinasamakiCounter(
                modifier = Modifier,
                count = clockTime.weekDay,
                sayInstead = convertWeekDay(clockTime.weekDay, true, context),
                useSayInstead = true,
                singleRoll = true,
                forceSlideIncrease = true,
            )
                Text(
                    modifier = Modifier,
                    text = ", ",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.year,
                    forceSlideIncrease = true,
                )
                Text(
                    modifier = Modifier,
                    text = "-",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.month + 1,
                    forceSlideIncrease = true,
                )
                Text(
                    modifier = Modifier,
                    text = "-",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.day,
                    forceSlideIncrease = true,
                )

                // space
                Text(
                    modifier = Modifier,
                    text = " | ",
                )
            }

            //time
            SinasamakiCounter(
                modifier = Modifier,
                count = clockTime.hours,
                forceSlideIncrease = true,

            )
            Text(
                modifier = Modifier,
                text = ":",
            )
            SinasamakiCounter(
                modifier = Modifier,
                count = clockTime.minutes,
                forceSlideIncrease = true,
            )
            Text(
                modifier = Modifier,
                text = ":",
            )
            SinasamakiCounter(
                modifier = Modifier,
                count = clockTime.seconds,
                forceSlideIncrease = true,
            )
        }
    } else
    {
        Column(
            modifier = modifier
                .combinedClickable(
                    onClick ={

                        onClick()
                    },
                    onLongClick = {
                        val readout = context.resources.getString(R.string.action_telltime,timeFormat,dateFormat)
                        ttsSpeakOrStop(
                            handover = tts,
                            message = readout
                        )
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast
                            .makeText(
                                context,
                                readout,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        onLongClick()
                    },
                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.hours,
                    forceSlideIncrease = true,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    modifier = Modifier,
                    text = ".",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.minutes,
                    forceSlideIncrease = true,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    modifier = Modifier,
                    text = ".",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.seconds,
                    forceSlideIncrease = true,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
//            OutlinedCard(
//
//            ) {
//                Row {
//                    Text(
//                        modifier = Modifier,
//                        text = "Y",
//                    )
//                    SinasamakiCounter(
//                        modifier = Modifier,
//                        count = clockTime.year,
//                        forceSlideIncrease = true,
//                    )
//                }
//            }
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.weekDay,
                    sayInstead = convertWeekDay(clockTime.weekDay, true, context),
                    useSayInstead = true,
                    singleRoll = true,
                    forceSlideIncrease = true,
                )
                Text(
                    modifier = Modifier,
                    text = ", ",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.year,
                    forceSlideIncrease = true,
                )
                Text(
                    modifier = Modifier,
                    text = "-",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.month+1,
                    forceSlideIncrease = true,
                )
                Text(
                    modifier = Modifier,
                    text = "-",
                )
                SinasamakiCounter(
                    modifier = Modifier,
                    count = clockTime.day,
                    forceSlideIncrease = true,
                )
            }
        }
    }


}

fun convertWeekDay(weekDay:Int = 0, short:Boolean = false, context: Context):String{
    return when(weekDay){
        1 -> if(short) context.getString(R.string.action_weekDay_1_short) else context.getString(R.string.action_weekDay_1_full)
        2 -> if(short) context.getString(R.string.action_weekDay_2_short) else context.getString(R.string.action_weekDay_2_full)
        3 -> if(short) context.getString(R.string.action_weekDay_3_short) else context.getString(R.string.action_weekDay_3_full)
        4 -> if(short) context.getString(R.string.action_weekDay_4_short) else context.getString(R.string.action_weekDay_4_full)
        5 -> if(short) context.getString(R.string.action_weekDay_5_short) else context.getString(R.string.action_weekDay_5_full)
        6 -> if(short) context.getString(R.string.action_weekDay_6_short) else context.getString(R.string.action_weekDay_6_full)
        7 -> if(short) context.getString(R.string.action_weekDay_7_short) else context.getString(R.string.action_weekDay_7_full)
        else -> if(short) context.getString(R.string.action_weekDay_0_short) else context.getString(R.string.action_weekDay_0_full)
    }
}

fun convertMonth(month:Int = 0, short:Boolean = false, context: Context):String{
    return when(month){
        1 -> if(short) context.getString(R.string.action_month_1_short) else context.getString(R.string.action_month_1_full)
        2 -> if(short) context.getString(R.string.action_month_2_short) else context.getString(R.string.action_month_2_full)
        3 -> if(short) context.getString(R.string.action_month_3_short) else context.getString(R.string.action_month_3_full)
        4 -> if(short) context.getString(R.string.action_month_4_short) else context.getString(R.string.action_month_4_full)
        5 -> if(short) context.getString(R.string.action_month_5_short) else context.getString(R.string.action_month_5_full)
        6 -> if(short) context.getString(R.string.action_month_6_short) else context.getString(R.string.action_month_6_full)
        7 -> if(short) context.getString(R.string.action_month_7_short) else context.getString(R.string.action_month_7_full)
        8 -> if(short) context.getString(R.string.action_month_8_short) else context.getString(R.string.action_month_8_full)
        9 -> if(short) context.getString(R.string.action_month_9_short) else context.getString(R.string.action_month_9_full)
        10 -> if(short) context.getString(R.string.action_month_10_short) else context.getString(R.string.action_month_10_full)
        11 -> if(short) context.getString(R.string.action_month_11_short) else context.getString(R.string.action_month_11_full)
        12 -> if(short) context.getString(R.string.action_month_12_short) else context.getString(R.string.action_month_12_full)
        else -> if(short) context.getString(R.string.action_month_0_short) else context.getString(R.string.action_month_0_full)
    }
}

fun fuzzyLegacyDate(clockTime:Time, context: Context):String{
    // fuck this shit, no solution!
    return "${convertWeekDay(
        clockTime.weekDay,
        short = false,
        context = context,
    )}, ${clockTime.day} ${convertMonth(clockTime.month, false, context)} ${clockTime.year}"
}

fun fuzzyLegacyTime(clockTime:Time, context: Context):String{
    return "${clockTime.hours}.${clockTime.minutes}"
}

data class Time(
    // zsmb's selfmade class
    // https://github.com/zsmb13/ComposeClock/blob/main/app/src/main/java/co/zsmb/composeclock/MainActivity.kt
    val hoursTwelve :Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val amPm:Int = 0,
    val formatFull:String = "",
    val sayFull:String = "00.00:00",
    val sayPart:String = "00.00",
    val sayFullTwelve:String = "00.00:00 AM",
    val sayPartTwelve:String = "00.00 AM",
    // Well we got to make ones too for date
    val weekDay: Int = 0,
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val sayISO: String = "YYYY-MM-DD",
    val sayISOFull: String = "WWWW, YYYY-MM-DD",
    // Finally,
    val instance:Calendar = Calendar.getInstance(),
)
//data class Date(
//    // Well we got to make ones too for date
//    val weekDay: Int = 0,
//    val day: Int = 0,
//    val month: Int = 0,
//    val year: Int = 0,
//    val formatFull: String = "",
//    val sayISO: String = "YYYY-MM-DD",
//    val sayISOFull: String = "WWWW, YYYY-MM-DD",
//)

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
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )
                            HTViewClock(
                                minimalized = true,
                                modifier = Modifier
                            )
                        }

                    }

                }

            }
        }
    }
}