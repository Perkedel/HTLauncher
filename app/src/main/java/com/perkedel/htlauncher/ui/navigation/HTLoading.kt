package com.perkedel.htlauncher.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun HTLoading(
    modifier: Modifier = Modifier,
){
    // https://developer.android.com/develop/ui/compose/components/progress
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(10.dp)
                ,

            )
        }
    }
}

@HTPreviewAnnotations
@Composable
fun HTLoadingPreview(){
    HTLauncherTheme {
        HTLoading()
    }
}