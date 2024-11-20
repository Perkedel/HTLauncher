package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class ConfigSelected(@StringRes val title: Int) {
    Donation(title = R.string.donation_option),
    LevelEditor(title = R.string.editor_screen),
    ItemsExplorer(title = R.string.items_explorer_screen)
}