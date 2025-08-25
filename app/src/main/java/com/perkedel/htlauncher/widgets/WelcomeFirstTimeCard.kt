@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.perkedel.htlauncher.widgets

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun WelcomeFirstTimeCard(
    context:Context = LocalContext.current,
    onChooseSaveDir: () -> Unit = {},
    onGoToSetting: () -> Unit = {},
    onTryDemo: () -> Unit = {},
    content: @Composable () -> Unit = {},
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ,
    ){
        Column(
            modifier = Modifier
                // https://stackoverflow.com/a/68166668/9079640
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.NewReleases,
                    contentDescription = "New"
                )
            }
            Text(
//                text = stringResource(R.string.FirstTimer_Welcome),
                text = context.getString(R.string.FirstTimer_Welcome, stringResource(R.string.app_name)),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(16.dp)
            )
            Text(
                modifier = Modifier,
                text = context.getString(R.string.FirstTimer_Welcome_Description, stringResource(R.string.FirstTimer_RecommendedDirectory)),
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(16.dp)
            )
            HTButton(
                modifier = Modifier
                    .fillMaxWidth()
                        ,
                title = stringResource(R.string.FirstTimer_Welcome_SetupSaveButton),
                leftIcon = Icons.Default.Save,
                onClick = onChooseSaveDir
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(16.dp)
            )
            HTButton(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                buttonType = ButtonTypes.OutlineButton,
                title = stringResource(R.string.FirstTimer_Welcome_SettingButton),
                leftIcon = Icons.Default.Settings,
                onClick = onGoToSetting
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(16.dp)
            )
            HTButton(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                buttonType = ButtonTypes.OutlineButton,
                title = stringResource(R.string.FirstTimer_Welcome_DemoButton),
                leftIcon = Icons.Default.Preview,
                onClick = onTryDemo,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@HTPreviewAnnotations
@Composable
fun PreviewWelcomeFirstTimeCard(

){
    HTLauncherTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                HTAppBar()
            }
        ) {
            padding ->
            Box(
                modifier = Modifier.padding(padding),
                contentAlignment = Alignment.Center
            ){
                WelcomeFirstTimeCard();
            }
        }
    }
}