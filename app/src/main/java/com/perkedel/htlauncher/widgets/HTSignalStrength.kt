@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LteMobiledata
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SignalCellular0Bar
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularAlt1Bar
import androidx.compose.material.icons.filled.SignalCellularAlt2Bar
import androidx.compose.material.icons.filled.SignalCellularNull
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.func.SignalStrength
import com.perkedel.htlauncher.func.SignalType
import com.perkedel.htlauncher.func.signalStrength
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun HTSignalStrength(
    modifier: Modifier = Modifier,
    context:Context = LocalContext.current,
    forWhichSim:Int = 0,
    showNumbers:Boolean = false,
    showCarrier:Boolean = false,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onClick: ()->Unit = {},
    onLongClick: ()->Unit= {},
    haptic: HapticFeedback = LocalHapticFeedback.current,
){
    var signalStatus:SignalStrength by remember { mutableStateOf(SignalStrength()) }
    LaunchedEffect(0) {
        while (true) {
            signalStatus = signalStrength(context = context)
            delay(5000L)
        }
    }

    val cellBar:ImageVector = when(signalStatus.signalLevel){
        0 -> Icons.Default.SignalCellular0Bar
        1 -> Icons.Default.SignalCellularAlt1Bar
        2 -> Icons.Default.SignalCellularAlt2Bar
        3 -> Icons.Default.SignalCellularAlt
        4 -> Icons.Default.SignalCellular4Bar
        else -> Icons.Default.SignalCellularNull
    }
    val cellBarSay:String = when(signalStatus.signalLevel){
        0 -> context.resources.getString(R.string.action_signal_0)
        1 -> context.resources.getString(R.string.action_signal_1)
        2 -> context.resources.getString(R.string.action_signal_2)
        3 -> context.resources.getString(R.string.action_signal_3)
        4 -> context.resources.getString(R.string.action_signal_4)
        else -> context.resources.getString(R.string.action_signal_unknown)
    }
    val cellBarDrawing:Int = when(signalStatus.signalLevel){
        // wtf Google, why no these icon in extended even?!??!?!??!??!?!
        0 -> R.drawable.sharp_signal_cellular_0_bar_24
        1 -> R.drawable.sharp_signal_cellular_1_bar_24
        2 -> R.drawable.sharp_signal_cellular_2_bar_24
        3 -> R.drawable.sharp_signal_cellular_3_bar_24
        4 -> R.drawable.sharp_signal_cellular_4_bar_24
        else -> R.drawable.sharp_signal_cellular_null_24
    }
    val cellNetwork:ImageVector = when(signalStatus.signalType){
        SignalType.LTE -> Icons.Default.LteMobiledata
        else -> Icons.Default.QuestionMark
    }
    val cellNetworkSay:String = signalStatus.signalType.sayShort

    Column(
        modifier = modifier
            .combinedClickable(
                onClick = {

                },
                onLongClick = {
                    val readout = "${context.resources.getString(R.string.action_tellcarrier, signalStatus.carrier)}, ${
                        context.resources.getString(
                            R.string.action_tellsignal,
                            cellBarSay,
                            signalStatus.signalDbm,
                            signalStatus.signalAsu
                        )
                    }"
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
            ),
    ) {
//        Row {
//            Text(text = "Signal: ${signalStatus.signalLevel}\n(${signalStatus.signalDbm}, ${signalStatus.signalType.name})")
//        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
//                    .size(48.dp)
                    .sizeIn(
                        minHeight = 0.dp,
                        minWidth = 0.dp,
                        maxHeight = 48.dp,
                        maxWidth = 48.dp,
                    )
                ,
            ){
                Text(
                    text = cellNetworkSay,
//                    fontSize = 24.sp,
                )
//                Icon(
//                    modifier = Modifier
//                        .size(48.dp)
//                    ,
//                    imageVector = cellBar,
//                    contentDescription = "",
//                )
                AsyncImage(
                    modifier = Modifier
//                        .size(48.dp)
//                        .fillMaxSize()
                    ,
                    model = cellBarDrawing,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(rememberColorScheme().onBackground),
                    error = painterResource(R.drawable.mavrickle),
                    placeholder = painterResource(R.drawable.sharp_signal_cellular_null_24)
                )
            }
            if(showNumbers) {
                Column(
                    modifier = Modifier
//                        .fillMaxHeight()
                    ,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "${signalStatus.signalDbm} dBm")
                    Text(text = "${signalStatus.signalAsu} asu")
                }
            }
            if(showCarrier){
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
//                            .weight(1f)
                            .basicMarquee()
                        ,
                        text = signalStatus.carrier
                    )
                }

            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun HTSignalStrengthPreview(){
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
                            HTSignalStrength(
                                showNumbers = true,
                                showCarrier = true,
                            )
                        }

                    }

                }

            }
        }
    }
}