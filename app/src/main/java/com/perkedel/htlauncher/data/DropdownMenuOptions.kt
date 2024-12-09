package com.perkedel.htlauncher.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.ui.graphics.vector.ImageVector

data class DropdownMenuOptions(
    val key: String = "key",
    val label: String = "Action",
    val icon: ImageVector = Icons.Default.Circle,
    val onClick: () -> Unit = {},
)
