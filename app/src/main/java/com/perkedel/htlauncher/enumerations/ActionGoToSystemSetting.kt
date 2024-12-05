package com.perkedel.htlauncher.enumerations

import android.provider.Settings

enum class ActionGoToSystemSetting(val route: String = Settings.ACTION_SETTINGS) {
    Default(route = Settings.ACTION_SETTINGS),
    Location(route = Settings.ACTION_LOCATION_SOURCE_SETTINGS),
    Bluetooth(route = Settings.ACTION_BLUETOOTH_SETTINGS),
    AddAccount(route = Settings.ACTION_ADD_ACCOUNT),
    Accessibility(route = Settings.ACTION_ACCESSIBILITY_SETTINGS),
}