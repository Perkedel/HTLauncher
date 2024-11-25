@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.perkedel.htlauncher.ui.dialog

import android.content.Context
import android.graphics.drawable.Icon
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.enumerations.ThirdButtonPosition
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.DialogPreviewKind
import com.perkedel.htlauncher.ui.previews.DialogPreviewParameter
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.HTButton

@Composable
fun HTAlertDialog(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    selectIcon: ImageVector = Icons.Default.Error,
    selectIconDescriptor:String = "Icon",
    icon: @Composable () -> Unit = {
        Icon(
            modifier = Modifier

            ,
            imageVector = selectIcon,
            contentDescription = selectIconDescriptor
        )
    },
    title:String = "Alert",
    text:String = "This is alert",

    confirmText:String = context.resources.getString(R.string.confirm_button), // YES
    dismissText:String = context.resources.getString(R.string.dismiss_button), // CANCEL
    thirdText:String = context.resources.getString(R.string.third_button), // NO
    // https://stackoverflow.com/questions/74044246/how-to-get-stringresource-if-not-in-a-composable-function

    swapButton:Boolean = false,
    thirdButton:Boolean = false,
    thirdButtonPosition: ThirdButtonPosition = ThirdButtonPosition.Middle,
    confirmButton:Boolean = true, // false will hide it
    confirmButtonDismiss:Boolean = false,
    hideIcon:Boolean = false,
    dismissButton:Boolean = true,

    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onThirdButton: () -> Unit = {}, // onSecondThought

    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),

    content: @Composable () -> Unit = {},
){
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    ,
            ) {
                Column(
                    modifier = Modifier
                        // https://stackoverflow.com/a/68166668/9079640
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    if(!hideIcon) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            icon()
                        }
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .size(16.dp)
                        )
                    }

                    Text(
                        title,
                        modifier = if(!hideIcon) Modifier.align(Alignment.CenterHorizontally) else Modifier,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )

                    Text(
//                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        modifier = Modifier,
                        text = text,
//                        textAlign = TextAlign.Center
                    )
//                    Spacer(
//                        Modifier
//                            .fillMaxWidth()
//                            .size(16.dp)
//                    )

                    content()
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .horizontalScroll(rememberScrollState())
                        ,
                    ) {
                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Left){
                            HTButton(
                                title = thirdText,
                                onClick = onThirdButton,
                                buttonType = ButtonTypes.TextButton
                            )
                        }

                        if(swapButton){
                            if(confirmButton) {
                                HTButton(
                                    title = confirmText,
                                    onClick = if(!confirmButtonDismiss) onConfirm else onDismissRequest,
                                    buttonType = ButtonTypes.TextButton
                                )
                            }
                        } else {
                            if(dismissButton) {
                                HTButton(
                                    title = dismissText,
                                    onClick = onDismissRequest,
                                    buttonType = ButtonTypes.TextButton
                                )
                            }
                        }

                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Middle){
                            HTButton(
                                title = thirdText,
                                onClick = onThirdButton,
                                buttonType = ButtonTypes.TextButton
                            )
                        }

                        if(swapButton){
                            if(dismissButton) {
                                HTButton(
                                    title = dismissText,
                                    onClick = onDismissRequest,
                                    buttonType = ButtonTypes.TextButton
                                )
                            }
                        }
                        else {
                            if(confirmButton) {
                                HTButton(
                                    title = confirmText,
                                    onClick = if(!confirmButtonDismiss) onConfirm else onDismissRequest,
                                    buttonType = ButtonTypes.TextButton
                                )
                            }
                        }

                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Right){
                            HTButton(
                                title = thirdText,
                                onClick = onThirdButton,
                                buttonType = ButtonTypes.TextButton
                            )
                        }
                    }
                }
            }
        }
    )
}

@HTPreviewAnnotations
@Composable
fun HTAlertDialogPreview(
    @PreviewParameter(DialogPreviewParameter::class) dialogKind:DialogPreviewKind
){
    HTLauncherTheme {
        Scaffold(
            modifier = Modifier,
            topBar = { HTAppBar() },
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                HTAlertDialog(
                    title = "Alert\nOHno",
                    text = "Ho\nHu\nHi\nHe\nHa",
                    thirdButton = dialogKind.thirdButton,
                    hideIcon = dialogKind.hideIcon,
                    thirdButtonPosition = dialogKind.thirdButtonPosition,
                    confirmButton = dialogKind.confirmButton,
                    dismissButton = dialogKind.dismissButton,
                )
            }
        }

    }
}