@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeak
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun SettingCategoryBar(
    title:String = "",
    modifier: Modifier = Modifier,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    context: Context = LocalContext.current,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    icon: @Composable() (() -> Unit?)? = null,
    view: View = LocalView.current,
){
    val readout:String = stringResource(R.string.category_bar_fill, title)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                },
                onLongClick = {
                    // DONE: Talkback read this title!
                    ttsSpeak(
                        handover = tts,
                        message = readout
                    )
                    Toast.makeText(context, readout, Toast.LENGTH_SHORT).show()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onClickLabel = readout
            ),
    ) {
        Row {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
                    .align(Alignment.CenterVertically)
                ,
                text = title
            )
            if (icon != null) {
                // ikr? just eval `(icon)` does not work unlike in most game engines. What a
                Box(
                    modifier = Modifier.padding(25.dp)
                ) {
                    icon()
                }
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun SettingCategoryBarPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier.statusBarsPadding().navigationBarsPadding()
        ) {
            LazyColumn {
                items(5){
                    SettingCategoryBar(
                        title = "Category ${it}",
                        icon = {
                            Icon(Icons.Default.Circle,"")
                        }
                    )
                }
            }
        }
    }
}