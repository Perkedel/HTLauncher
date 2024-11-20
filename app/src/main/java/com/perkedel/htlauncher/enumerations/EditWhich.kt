package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.perkedel.htlauncher.R

enum class EditWhich(@StringRes val select:Int, @StringRes val label:Int) {
    Pages(select = R.string.pages_folder, label = R.string.pages_folder_label),
    Items(select = R.string.items_folder, label = R.string.items_folder_label),
    Themes(select = R.string.themes_folder, label = R.string.themes_folder_label),
    Medias(select = R.string.medias_folder, label = R.string.medias_folder_label),
}