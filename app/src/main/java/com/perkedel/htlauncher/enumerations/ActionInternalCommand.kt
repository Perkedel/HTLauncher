package com.perkedel.htlauncher.enumerations

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.perkedel.htlauncher.R

enum class ActionInternalCommand(@StringRes val id:Int = 0, @StringRes val label:Int = 0, val icon:ImageVector){
    AllApps(id = R.string.internal_commands_all_apps, label = R.string.internal_commands_all_apps_label, icon = Icons.Default.Apps),
    Settings(id = R.string.internal_commands_settings, label = R.string.internal_commands_settings_label, icon = Icons.Default.Settings),
    Camera(id = R.string.internal_commands_camera, label = R.string.internal_commands_camera_label, icon = Icons.Default.Camera),
    Gallery(id = R.string.internal_commands_gallery, label = R.string.internal_commands_gallery_label, icon = Icons.Default.Image),
    Telephone(id = R.string.internal_commands_telephone, label = R.string.internal_commands_telephone_label, icon = Icons.Default.Phone),
    Contacts(id = R.string.internal_commands_contacts, label = R.string.internal_commands_contacts_label, icon = Icons.Default.Contacts),
    Clock(id = R.string.internal_commands_clock, label = R.string.internal_commands_clock_label, icon = Icons.Default.AccessTime),
    Messages(id = R.string.internal_commands_messages, label = R.string.internal_commands_messages_label, icon = Icons.AutoMirrored.Default.Message),
    Emergency(id = R.string.internal_commands_emergency, label = R.string.internal_commands_emergency_label, icon = Icons.Default.Emergency),
    Aria(id = R.string.internal_commands_aria, label = R.string.internal_commands_aria_label, icon = Icons.Default.Accessibility), // Read aloud label or aria field on click
    GoToPage(id = R.string.internal_commands_GoToPage, label = R.string.internal_commands_GoToPage_label, icon = Icons.Default.Pages), // Go to page which in this homescreen
    OpenAPage(id = R.string.internal_commands_OpenAPage, label = R.string.internal_commands_OpenAPage_label, icon = Icons.AutoMirrored.Default.MenuOpen), // Open a launcher page in a dialog
}