package com.perkedel.htlauncher.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun HTLoading(
    modifier: Modifier = Modifier,
){
    // https://developer.android.com/develop/ui/compose/components/progress
    Box {
        Card(

        ) {

        }
    }
}

@Composable
fun HTLoadingPreview(){
    HTLauncherTheme {
        HTLoading()
    }
}