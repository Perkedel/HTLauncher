@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.perkedel.htlauncher.widgets

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun SharedTransitionScope.ContainsSharedTransition(
    animatedVisibilityScope: AnimatedVisibilityScope,
    content: @Composable SharedTransitionScope.() -> Unit = {}
){
    // https://github.com/philipplackner/SharedElementTransitionCompose/blob/master/app/src/main/java/com/plcoding/sharedelementtransitioncompose/ListScreen.kt
    // https://youtu.be/mE5bLb42_Os Phillip Lackner
    // https://youtu.be/F3AYIuajrng Stevdza-san
    content()
}

@HTPreviewAnnotations
@Composable
fun SharedTransitionScope.PreviewContainsSharedTransition(){
    val navController = rememberNavController()
    HTLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
        ) {
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = "list"
                ){
                    composable(
                        route = "list"
                    ){
                        ContainsSharedTransition(
                            animatedVisibilityScope = this@composable,
                        ){
                            Box {
                                Text("Hello")
                            }
                        }
                    }

                }
            }
        }
    }
}