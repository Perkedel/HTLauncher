package com.perkedel.htlauncher.modules

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import java.util.Locale

// https://serge-hulne.medium.com/how-to-do-text-to-speech-the-easy-way-with-android-kotlin-compose-2024-628015d4c5c2

@Composable
fun rememberTextToSpeech(
    locale: Locale = Locale.getDefault()
): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = locale
            }
        }
        tts.value = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}

fun ttsSpeak(handover:MutableState<TextToSpeech?>, message:String = "",queueMode: Int = TextToSpeech.QUEUE_FLUSH, params:Bundle? = null, utteranceId:String = ""){
    handover.value?.speak(message,queueMode, params, utteranceId)
}

fun ttsSpeakOrStop(handover:MutableState<TextToSpeech?>, message:String = "",queueMode: Int = TextToSpeech.QUEUE_FLUSH, params:Bundle? = null, utteranceId:String = ""){
    if(handover.value?.isSpeaking == true){
        handover.value?.stop()
    } else {
        ttsSpeak(
            handover = handover,
            message = message,
            queueMode = queueMode,
            params = params,
            utteranceId = utteranceId,
        )
    }
}