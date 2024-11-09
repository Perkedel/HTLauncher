package com.perkedel.htlauncher.ui.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.perkedel.htlauncher.widgets.FirstPageCard

@Composable
fun BasePage(
    isFirstPage: Boolean = false,
    onMoreMenuButtonClicked: () -> Unit,
    modifier: Modifier.Companion,
){
    // Permanent Card on first page
    if (isFirstPage){
        FirstPageCard(
            modifier = Modifier.fillMaxWidth(),
            onMoreMenuButton = onMoreMenuButtonClicked,
        )
    } else {
        // idk
    }
}