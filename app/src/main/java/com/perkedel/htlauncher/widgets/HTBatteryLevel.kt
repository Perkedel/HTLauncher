@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.func.SystemBroadcastReceiver
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.WarnColorDark
import com.perkedel.htlauncher.ui.theme.WarnColorDarkContainer
import com.perkedel.htlauncher.ui.theme.WarnColorLight
import com.perkedel.htlauncher.ui.theme.WarnColorLightContainer
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun HTBatteryLevel(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    circular:Boolean = false,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onClick: ()->Unit = {},
    onLongClick: ()->Unit= {},
    haptic: HapticFeedback = LocalHapticFeedback.current,
){
    // https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/ProgressIndicatorSamples.kt
    // https://www.geeksforgeeks.org/get-battery-level-in-android-using-jetpack-compose/
    // https://github.com/MatthiasKerat/BatteryYT/blob/main/app/src/main/java/com/kapps/batteryyt/MainActivity.kt
    // https://developer.android.com/training/monitoring-device-state/battery-monitoring
    // https://github.com/BharathVishal/BatteryStats-for-Android
    // https://github.com/BharathVishal/BatteryStats-for-Android
    // https://developer.android.com/reference/android/os/BatteryManager#EXTRA_LEVEL
    // https://stackoverflow.com/questions/75579909/how-can-i-update-the-value-i-draw-with-jetpack-compose
    var batteryLevel:Int by remember { mutableStateOf(70) }
    var isCharging:Boolean by remember { mutableStateOf(true) }
    var temperature:Int by remember { mutableStateOf(30) }
    var isOverheated:Boolean by remember { mutableStateOf(false) }
    var chargeRemaining:Long = 0L
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_CHANGED) { batteryStatus ->
        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        val isChargingGet:Boolean? = batteryStatus?.let { intent ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                intent.getBooleanExtra(BatteryManager.EXTRA_CHARGING_STATUS, false)
            } else {
                intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) >= BatteryManager.BATTERY_PLUGGED_AC
            }
        }
        temperature = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
//        chargeRemaining =
        isCharging = isChargingGet ?: false
        batteryLevel = batteryPct!!.toInt()
    }
//    if(!LocalInspectionMode.current) {
//        val batteryManager: BatteryManager =
//            context.getSystemService(BATTERY_SERVICE) as BatteryManager
////        LaunchedEffect(
////            key1 = batteryLevel
////        ) {
////            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
////        }
//        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//    }
    val animateLevel by
    animateFloatAsState(
        targetValue = batteryLevel.toFloat()/100,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "BatteryAnimatedLevel",
    )

    val linearTrackColor: Color =
        if(batteryLevel > 50)
            ProgressIndicatorDefaults.linearTrackColor
        else if(batteryLevel > 15)
            // fade orange!
            if(isSystemInDarkTheme()){
                WarnColorDarkContainer
            } else {
                WarnColorLightContainer
            }
        else
            rememberColorScheme().errorContainer
    val circularTrackColor: Color =
        if(batteryLevel > 50)
            ProgressIndicatorDefaults.circularDeterminateTrackColor
        else if(batteryLevel > 15)
            if(isSystemInDarkTheme()){
                WarnColorDarkContainer
            } else {
                WarnColorLightContainer
            }
        else
            rememberColorScheme().errorContainer
    val linearColor: Color =
        if(batteryLevel > 50)
            ProgressIndicatorDefaults.linearColor
        else if(batteryLevel > 15)
            // color orange!!! thancc codeium
            if(isSystemInDarkTheme()){
                WarnColorDark
            } else {
                WarnColorLight
            }
        else
            rememberColorScheme().error
    val circularColor: Color =
        if(batteryLevel > 50)
            ProgressIndicatorDefaults.circularColor
        else if(batteryLevel > 15)
            if(isSystemInDarkTheme()){
                WarnColorDark
            } else {
                WarnColorLight
            }
        else
            rememberColorScheme().error

    val textFillColor: Color =
        if(batteryLevel > 40)
            rememberColorScheme().onTertiary
        else
            rememberColorScheme().tertiary

    val textOutlineColor: Color =
        if(batteryLevel > 40)
            rememberColorScheme().onTertiaryContainer
        else
            rememberColorScheme().tertiaryContainer

    val sayShouldCharge:String = if(isCharging) {
        when{
            batteryLevel >= 100 -> context.getString(R.string.action_batteryfull)
            else -> context.getString(R.string.action_batterycharging)
        }
    }
    else {
        when {
            batteryLevel in 16..39 -> context.getString(R.string.action_batterymedium)
            batteryLevel <= 15 -> context.getString(R.string.action_batterylow)
            else -> ""
        }
    }

    Box(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    val readout = context.resources.getString(R.string.action_tellbattery,batteryLevel) + ". $sayShouldCharge"
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
        contentAlignment = Alignment.Center,
    ){
        if(circular){
            CircularProgressIndicator(
                modifier = Modifier,
                progress = { animateLevel },
                color = circularColor,
                trackColor = circularTrackColor
            )
            if(isCharging){
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = "",
                    modifier = Modifier
                        .size(16.dp)
//                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                    ,
//                    tint = rememberColorScheme().tertiaryContainer
                )
            }
            OutlinedText(
                text = "${batteryLevel}",
//                outlineColor = textOutlineColor,
//                fillColor = textFillColor,
                outlineDrawStyle = Stroke(
                    width = 1f,
                )
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.height(32.dp),
                progress = { animateLevel },
                color = linearColor,
                trackColor = linearTrackColor
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if(isCharging){
//                    OutlinedText(
//                        text = "⚡",
//                        outlineColor = textOutlineColor,
//                        fillColor = textFillColor,
//                        outlineDrawStyle = Stroke(
//                            width = 1f,
//                        )
//                    )
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "",
                        tint = textFillColor
                    )
                }
                OutlinedText(
                    text = "${batteryLevel}",
                    outlineColor = textOutlineColor,
                    fillColor = textFillColor,
                    outlineDrawStyle = Stroke(
                        width = 1f,
                    )
                )
            }

        }
    }

}

@HTPreviewAnnotations
@Composable
fun HTBatteryLevelPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier
        ) {
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
                                HTBatteryLevel(
                                    modifier = Modifier

                                    ,
                                )
                                HTBatteryLevel(
                                    modifier = Modifier

                                    ,
                                    circular = true,
                                )
                            }

                        }

                    }

                }
            }

        }
    }
}