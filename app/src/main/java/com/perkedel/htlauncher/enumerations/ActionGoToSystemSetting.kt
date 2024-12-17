package com.perkedel.htlauncher.enumerations

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class ActionGoToSystemSetting(val route: String = Settings.ACTION_SETTINGS, val id:String = "", @StringRes val label:Int = 0, @DrawableRes val icon:Int = R.drawable.settings_gear) {
    // TODO: add labels & icons to resource!
    Default(route = Settings.ACTION_SETTINGS, id = "SettingsDefault"),
    Location(route = Settings.ACTION_LOCATION_SOURCE_SETTINGS, id = "SettingsLocation"),
    Bluetooth(route = Settings.ACTION_BLUETOOTH_SETTINGS, id = "SettingsBluetooth"),
    WiFi(route = Settings.ACTION_WIFI_SETTINGS, id = "SettingsWiFi"),
    AddAccount(route = Settings.ACTION_ADD_ACCOUNT, id = "SettingsAddAccount"),
    Accessibility(route = Settings.ACTION_ACCESSIBILITY_SETTINGS, id = "SettingsAccessibility"),
    AirplaneMode(route = Settings.ACTION_AIRPLANE_MODE_SETTINGS, id = "SettingsAirplaneMode"),
    ApplicationDetails(route = Settings.ACTION_APPLICATION_DETAILS_SETTINGS, id = "SettingsApplicationDetails"),
    @SuppressLint("InlinedApi")
    BatterySaver(route = Settings.ACTION_BATTERY_SAVER_SETTINGS, id = "SettingsBatterySaver"),
    Date(route = Settings.ACTION_DATE_SETTINGS, id = "SettingsDate"),
    Display(route = Settings.ACTION_DISPLAY_SETTINGS, id = "SettingsDisplay"),
    Home(route = Settings.ACTION_HOME_SETTINGS, id = "SettingsHome"),
    Keyboard(route = Settings.ACTION_INPUT_METHOD_SETTINGS, id = "SettingsKeyboard"),
    Locale(route = Settings.ACTION_LOCALE_SETTINGS, id = "SettingsLocale"),
    Privacy(route = Settings.ACTION_PRIVACY_SETTINGS, id = "SettingsPrivacy"),
    Security(route = Settings.ACTION_SECURITY_SETTINGS, id = "SettingsSecurity"),
    Sound(route = Settings.ACTION_SOUND_SETTINGS, id = "SettingsSound"),
    Storage(route = Settings.ACTION_INTERNAL_STORAGE_SETTINGS, id = "SettingsStorage"),
    Sync(route = Settings.ACTION_SYNC_SETTINGS, id = "SettingsSync"),
    // https://stackoverflow.com/a/21686120/9079640
    // https://stackoverflow.com/a/58545448/9079640
    Tethering(route = "com.android.settings.TetherSettings", id = "SettingsTethering"),
    UsageAccess(route = Settings.ACTION_USAGE_ACCESS_SETTINGS, id = "SettingsUsageAccess"),
    VoiceInput(route = Settings.ACTION_VOICE_INPUT_SETTINGS, id = "SettingsVoiceInput"),
    @SuppressLint("InlinedApi")
    Satelite(route = Settings.ACTION_SATELLITE_SETTING, id = "SettingsSatelite"),
    Applications(route = Settings.ACTION_APPLICATION_SETTINGS, id = "SettingsApplications"),
//    Network(route = Settings.ACTION_NETWORK_OPERATOR_SETTINGS, id = "SettingsApplications"),
//    Battery(route = Settings.Action_batt, id = "SettingsApplications"),
}