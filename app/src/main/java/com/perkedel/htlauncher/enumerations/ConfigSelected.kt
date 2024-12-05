package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class ConfigSelected(@StringRes val title: Int, val key:String = "") {
    Default(title = R.string.default_button, key = "default"),
    Donation(title = R.string.donation_option, key = "activation_license"),
    LevelEditor(title = R.string.editor_screen, key = "level_editor"),
    ItemsExplorer(title = R.string.items_explorer_screen, key = "item_editor")
}