package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class ConfigSelected(@StringRes val title: Int, val key:String = "") {
    Default(title = R.string.default_button, key = "default"),
    Donation(title = R.string.donation_option, key = "activation_license"),
    LevelEditor(title = R.string.editor_screen, key = "level_editor"),
    ItemsExplorer(title = R.string.items_explorer_screen, key = "item_editor"),
    GetStarted(title = R.string.default_screen, key = "get_started"),
    Display(title = R.string.default_screen, key = "display"),
    Accessibility(title = R.string.default_screen, key = "accessibility"),
    MenusSettings(title = R.string.default_screen, key = "menus"),
    ItemsSettings(title = R.string.default_screen, key = "items"),
    ContactsSettings(title = R.string.default_screen, key = "contacts"),
    TelephoneSettings(title = R.string.default_screen, key = "telephone"),
    MessagesSettings(title = R.string.default_screen, key = "messages"),
    EmergencySettings(title = R.string.default_screen, key = "emergency"),
    AppsSettings(title = R.string.default_screen, key = "apps"),
    Compatibility(title = R.string.default_screen, key = "compatibility"),
    RemoveDefault(title = R.string.default_screen, key = "remove_default"),
    UserManual(title = R.string.default_screen, key = "user_manual"),
}