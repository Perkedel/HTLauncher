@file:OptIn(ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.ui.dialog

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.Manifest
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionsTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean = true,
    onDismiss: () -> Unit =  {},
    onOkClick: () -> Unit = {},
    onGoToAppSettingClick: () -> Unit = {},
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
){
    // https://youtu.be/D3JCtaK8LSU
    HTAlertDialog(
        modifier = modifier,
        title = "Permission required",
        text = permissionsTextProvider.getDescription(isPermanentlyDeclined),
        confirmText = if(isPermanentlyDeclined) {"Grant Permission"} else {"OK"},
        confirmButtonDismiss = !isPermanentlyDeclined,
        onDismissRequest = onDismiss,
        onConfirm = if(!isPermanentlyDeclined) onGoToAppSettingClick else onOkClick,
        tts = tts,
    ) { }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean):String
}

class BasePermissionTextProvider: PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "It seems you permanently declined this permission.\nYou can go to the  app settings to grant it"
        } else {
            "This app needs this permission so the app can use the feature"
        }
    }
}

class ReadFilePermissionTextProvider: PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "It seems you permanently declined Read File Permission.\nYou can go to the  app settings to grant it"
        } else {
            "This app needs this permission so the app can Read files"
        }
    }
}

class WriteFilePermissionTextProvider: PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "It seems you permanently declined Write File Permission.\nYou can go to the  app settings to grant it"
        } else {
            "This app needs this permission so the app can Write files"
        }
    }
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "It seems you permanently declined camera permission.\nYou can go to the  app settings to grant it"
        } else {
            "This app needs access to your camera so that your phone can access camera"
        }
    }
}

class RecordAudioPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined microphone permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your microphone so that your friends " +
                    "can hear you in a call."
        }
    }
}

class PhoneCallPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined phone calling permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs phone calling permission so that you can talk " +
                    "to your friends."
        }
    }
}

class PhoneStatePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined phone state permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs phone state permission so that you can see " +
                    "your signal strength & cellular network type"
        }
    }
}

@HTPreviewAnnotations
@Composable
fun PermissionDialogPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),

        ) {
            PermissionDialog(
                permissionsTextProvider = BasePermissionTextProvider(),
                isPermanentlyDeclined = false,
            )
        }
    }

}