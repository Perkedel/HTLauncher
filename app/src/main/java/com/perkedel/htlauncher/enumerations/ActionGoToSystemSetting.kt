package com.perkedel.htlauncher.enumerations

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class ActionGoToSystemSetting(val route: String = Settings.ACTION_SETTINGS, val id:String = "", @StringRes val label:Int = R.string.internal_system_settings_placeholder, @DrawableRes val icon:Int = R.drawable.settings_gear) {
    // TODO: add labels & icons to resource!
    Default(route = Settings.ACTION_SETTINGS, id = "SettingsDefault"),
    Location(route = Settings.ACTION_LOCATION_SOURCE_SETTINGS, id = "SettingsLocation", label = R.string.internal_system_settings_location, icon = R.drawable.location_pin),
    Bluetooth(route = Settings.ACTION_BLUETOOTH_SETTINGS, id = "SettingsBluetooth", label = R.string.internal_system_settings_blueooth, icon = R.drawable.bluetooth),
    WiFi(route = Settings.ACTION_WIFI_SETTINGS, id = "SettingsWiFi", label = R.string.internal_system_settings_wifi, icon = R.drawable.wifi),
    // https://stackoverflow.com/a/21686120/9079640
    Hotspot(route = "android.settings.TetherSettings", id = "SettingsHotspot", label = R.string.internal_system_settings_hotspot, icon = R.drawable.router_hotspot),
    AddAccount(route = Settings.ACTION_ADD_ACCOUNT, id = "SettingsAddAccount"),
    Accessibility(route = Settings.ACTION_ACCESSIBILITY_SETTINGS, id = "SettingsAccessibility"),
    AirplaneMode(route = Settings.ACTION_AIRPLANE_MODE_SETTINGS, id = "SettingsAirplaneMode"),
    ApplicationDetails(route = Settings.ACTION_APPLICATION_DETAILS_SETTINGS, id = "SettingsApplicationDetails"),
    @SuppressLint("InlinedApi")
    BatterySaver(route = Settings.ACTION_BATTERY_SAVER_SETTINGS, id = "SettingsBatterySaver"),
    Date(route = Settings.ACTION_DATE_SETTINGS, id = "SettingsDate"),
    Display(route = Settings.ACTION_DISPLAY_SETTINGS, id = "SettingsDisplay", label = R.string.internal_system_settings_display, icon = R.drawable.display_monitor),
    Home(route = Settings.ACTION_HOME_SETTINGS, id = "SettingsHome"),
    Keyboard(route = Settings.ACTION_INPUT_METHOD_SETTINGS, id = "SettingsKeyboard"),
    Locale(route = Settings.ACTION_LOCALE_SETTINGS, id = "SettingsLocale"),
    Privacy(route = Settings.ACTION_PRIVACY_SETTINGS, id = "SettingsPrivacy", label = R.string.internal_system_settings_privacy, icon = R.drawable.privacy_bag),
    Security(route = Settings.ACTION_SECURITY_SETTINGS, id = "SettingsSecurity", label = R.string.internal_system_settings_security, icon = R.drawable.security_shield),
    Sound(route = Settings.ACTION_SOUND_SETTINGS, id = "SettingsSound", label = R.string.internal_system_settings_sound, icon = R.drawable.speaker_sound),
    Storage(route = Settings.ACTION_INTERNAL_STORAGE_SETTINGS, id = "SettingsStorage", label = R.string.internal_system_settings_storage, icon = R.drawable.storage_harddisk),
    Sync(route = Settings.ACTION_SYNC_SETTINGS, id = "SettingsSync"),
    // https://stackoverflow.com/a/21686120/9079640
    // https://stackoverflow.com/a/58545448/9079640
    Tethering(route = "com.android.settings.TetherSettings", id = "SettingsTethering", label = R.string.internal_system_settings_hotspot, icon = R.drawable.router_hotspot),
    UsageAccess(route = Settings.ACTION_USAGE_ACCESS_SETTINGS, id = "SettingsUsageAccess"),
    VoiceInput(route = Settings.ACTION_VOICE_INPUT_SETTINGS, id = "SettingsVoiceInput"),
    @SuppressLint("InlinedApi")
    Satellite(route = Settings.ACTION_SATELLITE_SETTING, id = "SettingsSatellite"),
    Applications(route = Settings.ACTION_APPLICATION_SETTINGS, id = "SettingsApplications", label = R.string.internal_system_settings_application, icon = R.drawable.app_setting),
//    Network(route = Settings.ACTION_NETWORK_OPERATOR_SETTINGS, id = "SettingsApplications"),
//    Battery(route = Settings.Action_batt, id = "SettingsApplications"),
}