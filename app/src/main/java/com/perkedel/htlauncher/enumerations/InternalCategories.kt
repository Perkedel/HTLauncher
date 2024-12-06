package com.perkedel.htlauncher.enumerations

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.perkedel.htlauncher.R

enum class InternalCategories(@StringRes val id:Int = 0, @StringRes val label:Int = 0, @DrawableRes val image:Int = 0, val icon: ImageVector) {
    Default(id = R.string.default_button, label = R.string.default_button, image = R.drawable.mavrickle, icon = Icons.Default.Settings),
    SettingsOverall(id = R.string.internal_categories_settings_overall, label = R.string.internal_categories_settings_overall_label, image = R.drawable.settings, icon = Icons.Default.Settings),
    SettingsSystem(id = R.string.internal_categories_settings_system, label = R.string.internal_categories_settings_system_label, image = R.drawable.settings_gear, icon = Icons.Default.Settings),
}