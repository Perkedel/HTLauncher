package com.perkedel.htlauncher.enumerations

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.ui.graphics.vector.ImageVector
import com.perkedel.htlauncher.R

enum class ActionInternalCommand(@StringRes val id:Int = 0, @StringRes val label:Int = 0, val icon:ImageVector){
    AllApps(id = R.string.internal_commands_all_apps, label = R.string.internal_commands_all_apps_label, icon = Icons.Default.Apps)
}