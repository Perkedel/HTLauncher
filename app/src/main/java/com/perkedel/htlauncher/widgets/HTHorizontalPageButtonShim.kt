@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.perkedel.htlauncher.widgets

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.LastPage
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun HTHorizontalPageButtonShim(
    modifier: Modifier = Modifier,
    hasFirstLastButton:Boolean = false,
    size: Dp = 25.dp,
    onLeftPage: () -> Unit = {},
    onRightPage: () -> Unit = {},
    onFirstPage: () -> Unit = {},
    onLastPage: () -> Unit = {},
    content: @Composable() (() -> Unit) = {},
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics {
                invisibleToUser()
            }
            .wrapContentSize()
            .fillMaxWidth()
        ,
    ) {
        if(hasFirstLastButton) {
            HTButton(
                modifier = Modifier
                    .focusProperties {
                        canFocus = false
                    }
                    .height(size)
                ,
                buttonType = ButtonTypes.TextButton,
                leftIcon = Icons.Default.FirstPage,
                onClick = onFirstPage
            )
        }
        HTButton(
            modifier = Modifier
                .focusProperties {
                    canFocus = false
                }
                .height(size)
            ,
            buttonType = ButtonTypes.TextButton,
            leftIcon = Icons.AutoMirrored.Default.ArrowLeft,
            onClick = onLeftPage
        )
        Spacer(
            modifier = Modifier
                .height(size)
                .weight(.1f)
            ,
        )
        Box(
            modifier = Modifier
                .height(size)
                .weight(1f)
                .wrapContentSize()
                .horizontalScroll(rememberScrollState())
            ,
        ){
            content()
        }
        Spacer(
            modifier = Modifier
                .height(size)
                .weight(.1f)
            ,
        )
        HTButton(
            modifier = Modifier
                .focusProperties {
                    canFocus = false
                }
                .height(size)
            ,
            buttonType = ButtonTypes.TextButton,
            rightIcon = Icons.AutoMirrored.Default.ArrowRight,
            onClick = onRightPage
        )
        if(hasFirstLastButton) {
            HTButton(
                modifier = Modifier
                    .focusProperties {
                        canFocus = false
                    }
                    .height(size)
                ,
                buttonType = ButtonTypes.TextButton,
                rightIcon = Icons.AutoMirrored.Default.LastPage,
                onClick = onLastPage
            )
        }
    }
}

@HTPreviewAnnotations
@Composable
fun HTHorizontalPageButtonShimPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
            ,
        ) {
            Box(

            ){
                HTHorizontalPageButtonShim(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                    ,
                    hasFirstLastButton = true,
                ){
                    HTHorizontalPageIndicators(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        pageCount = 10,
                        currentPage = 5,
                        targetPage = 5,
                        selectedIndicatorSize = 32.dp,
                        unselectedIndicatorSize = 18.dp,
                        indicatorCornerRadius = 24.dp,
                        textSize = 6.sp,
                        indicatorColor = rememberColorScheme().primary
                    )
                }
            }
        }
    }
}