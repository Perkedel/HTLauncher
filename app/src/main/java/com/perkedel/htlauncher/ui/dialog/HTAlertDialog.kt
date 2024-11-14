@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.ui.dialog

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.enumerations.ThirdButtonPosition
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun HTAlertDialog(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = { Icon(imageVector = Icons.Default.Error, contentDescription = "") },
    title:String = "Alert",
    text:String = "This is alert",

    confirmText:String = "Confirm", // YES
    dismissText:String = "Dismiss", // CANCEL
    thirdText:String = "Third", // NO

    swapButton:Boolean = false,
    thirdButton:Boolean = false,
    thirdButtonPosition: ThirdButtonPosition = ThirdButtonPosition.Middle,
    confirmButton:Boolean = true, // false will hide it
    confirmButtonDismiss:Boolean = false,

    content: @Composable () -> Unit = {},

    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onThirdButton: () -> Unit = {}, // onSecondThought
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
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    icon()

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )

                    Text(
                        title,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )
                    Text(
                        text = text,
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )

                    content()

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                        ,
                    ) {
                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Left){
                            TextButton(
                                onClick = onThirdButton
                            ) { Text(thirdText) }
                        }
                        if(swapButton){

                            if(confirmButton) {
                                TextButton(
                                    onClick = if(!confirmButtonDismiss) onConfirm else onDismissRequest
                                ) { Text(confirmText) }
                            }
                        } else {
                            TextButton(
                                onClick = onDismissRequest
                            ) { Text(dismissText) }
                        }
                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Middle){
                            TextButton(
                                onClick = onThirdButton
                            ) { Text(thirdText) }
                        }
                        if(swapButton){

                            TextButton(
                                onClick = onDismissRequest
                            ) { Text(dismissText) }
                        }
                        else {
                            if(confirmButton) {
                                TextButton(
                                    onClick = if(!confirmButtonDismiss) onConfirm else onDismissRequest
                                ) { Text(confirmText) }
                            }
                        }
                        if(thirdButton && thirdButtonPosition == ThirdButtonPosition.Right){
                            TextButton(
                                onClick = onThirdButton
                            ) { Text(thirdText) }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HTAlertDialogPreview(){
    HTLauncherTheme {
        HTAlertDialog(
            title = "Alert\nOHno",
            text = "Ho\nHu",
            thirdButton = true,
        )
    }
}