package com.perkedel.htlauncher.enumerations

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.ui.graphics.vector.ImageVector
import com.perkedel.htlauncher.R

enum class ActionInternalCommand(@StringRes val id:Int = 0, @StringRes val label:Int = 0, @DrawableRes val image:Int = 0, val icon:ImageVector){
    AllApps(id = R.string.internal_commands_all_apps, label = R.string.internal_commands_all_apps_label, image = R.drawable.all_apps, icon = Icons.Default.Apps),
    Settings(id = R.string.internal_commands_settings, label = R.string.internal_commands_settings_label, image = R.drawable.settings, icon = Icons.Default.SettingsApplications),
    SystemSettings(id = R.string.internal_commands_system_settings, label = R.string.internal_commands_system_settings_label, image = R.drawable.settings_gear, icon = Icons.Default.Settings),
    Preferences(id = R.string.internal_commands_preferences, label = R.string.internal_commands_preferences_label, image = R.drawable.preferences, icon = Icons.Default.Build),
    Camera(id = R.string.internal_commands_camera, label = R.string.internal_commands_camera_label, image = R.drawable.camera, icon = Icons.Default.Camera),
    Gallery(id = R.string.internal_commands_gallery, label = R.string.internal_commands_gallery_label, image = R.drawable.gallery, icon = Icons.Default.Image),
    Telephone(id = R.string.internal_commands_telephone, label = R.string.internal_commands_telephone_label, image = R.drawable.telephone, icon = Icons.Default.Phone),
    Contacts(id = R.string.internal_commands_contacts, label = R.string.internal_commands_contacts_label, image = R.drawable.contacts, icon = Icons.Default.Contacts),
    Clock(id = R.string.internal_commands_clock, label = R.string.internal_commands_clock_label, image = R.drawable.clock, icon = Icons.Default.AccessTime),
    Messages(id = R.string.internal_commands_messages, label = R.string.internal_commands_messages_label, image = R.drawable.messages, icon = Icons.AutoMirrored.Default.Message),
    Emergency(id = R.string.internal_commands_emergency, label = R.string.internal_commands_emergency_label, image = R.drawable.emergency, icon = Icons.Default.Emergency),
    Aria(id = R.string.internal_commands_aria, label = R.string.internal_commands_aria_label, image = R.drawable.aria, icon = Icons.Default.Accessibility), // Read aloud label or aria field on click
    GoToPage(id = R.string.internal_commands_GoToPage, label = R.string.internal_commands_GoToPage_label, image = R.drawable.go_to_page, icon = Icons.Default.Pages), // Go to page which in this homescreen
    OpenAPage(id = R.string.internal_commands_OpenAPage, label = R.string.internal_commands_OpenAPage_label, image = R.drawable.open_a_page, icon = Icons.AutoMirrored.Default.MenuOpen), // Open a launcher page in a dialog
}