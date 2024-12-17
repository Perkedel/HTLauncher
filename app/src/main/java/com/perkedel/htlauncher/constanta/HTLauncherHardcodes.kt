package com.perkedel.htlauncher.constanta

import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.ActionGoToSystemSetting
import com.perkedel.htlauncher.enumerations.ActionInternalCommand
import com.perkedel.htlauncher.enumerations.InternalCategories

object HTLauncherHardcodes {
    // https://stackoverflow.com/questions/77368662/kotlin-replace-only-last-given-string-from-string
    // https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/last-index-of-any.html
    // https://stackoverflow.com/a/50520935/9079640

    val PAGES_BUILTIN_FILES:List<String> = listOf(
        "Home",
        "Settings",
    )

    val HOMESCREEN_FILE:HomepagesWeHave = HomepagesWeHave(

    )
    val HOMEPAGE_FILE:PageData = PageData(
        name = "Home",
        isHome = true,
        items = listOf(
            "Telephone",
            "Contacts",
            "Messages",
            "Camera",
            "Gallery",
            "Clock",
            "SOS",
            "Settings",
            "AllApps"
        )
    )
    val ORGANIZER_PAGE_FILE:PageData = PageData(
        name = "Organizer",
        isHome = false,
        items = listOf(
            "Calendar",
            "Clock",
        )
    )
    val SETTINGS_PAGE_FILE:PageData = PageData(
        name = "Settings",
        isHome = false,
        items = listOf(
            "CategorySettingsOverall",
            "SettingsSystem",
            "Preferences",
            "CategorySettingsSystem",
            ActionGoToSystemSetting.WiFi.id,
            ActionGoToSystemSetting.Tethering.id,
            ActionGoToSystemSetting.Bluetooth.id,
            ActionGoToSystemSetting.Location.id,
            ActionGoToSystemSetting.Display.id,
            ActionGoToSystemSetting.Sound.id,
            ActionGoToSystemSetting.Security.id,
            ActionGoToSystemSetting.Applications.id,
//            ActionGoToSystemSetting.Battery.id,
            ActionGoToSystemSetting.Storage.id,
//            ActionGoToSystemSetting.Network.id,
        )
    )


    val ALLAPPS_FILE:ItemData = ItemData(
        name = "AllApps",
        label = "All Apps",
        aria = "All Apps",
        action = listOf(
            ActionData(
                name = "AllApps",
                action = "AllApps",
                type = ActionDataLaunchType.Internal,
            )
        )
    )
    val TELEPHONE_FILE:ItemData = ItemData(
        name = "Telephone",
        label = "Telephone",
        aria = "Telephone",
        action = listOf(
            ActionData(
                name = "Telephone",
                action = "Telephone",
                type = ActionDataLaunchType.Internal,
            )
        )
    )
    val CATEGORY_SETTINGS_OVERALL_FILE:ItemData = ItemData(
        name = "CategorySettingsOverall",
        label = "CategorySettingsOverall",
        aria = "CategorySettingsOverall",
        isCategory = true,
        action = listOf(
            ActionData(
                name = "CategorySettingsOverall",
                action = "CategorySettingsOverall",
                type = ActionDataLaunchType.Category,
            )
        )
    )
    val CATEGORY_SETTINGS_SYSTEM_FILE:ItemData = ItemData(
        name = "CategorySettingsSystem",
        label = "CategorySettingsSystem",
        aria = "CategorySettingsSystem",
        isCategory = true,
        action = listOf(
            ActionData(
                name = "CategorySettingsSystem",
                action = "CategorySettingsSystem",
                type = ActionDataLaunchType.Category,
            )
        )
    )


    val ALARM_APP_IMPLEMENTATIONS:Map<String,SearchableApps> = mapOf(
        // https://stackoverflow.com/a/4281243/9079640
        Pair("HTC",SearchableApps(packageName = "com.htc.android.worldclock")),
        Pair("AOSP",SearchableApps(packageName = "com.android.deskclock")),
        Pair("Froyo Nexus",SearchableApps(packageName = "com.google.android.deskclock")),
        Pair("Moto Blur",SearchableApps(packageName = "com.motorola.blur.alarmclock")),
        Pair("Samsung",SearchableApps(packageName = "com.sec.android.app.clockpackage")),
        Pair("Sony Ericsson",SearchableApps(packageName = "com.sonyericsson.organizer")),
        Pair("ASUS",SearchableApps(packageName = "com.asus.deskclock")),
        Pair("Example",SearchableApps(packageName = "com.example.deskclock")),
        Pair("rolodex",SearchableApps(packageName = "com.rolodex.rolodex")),
    )
    fun getInternalActionIcon(whichIs:String? = null):Int{
        return when(whichIs){
            ActionInternalCommand.AllApps.name -> R.drawable.all_apps
            ActionInternalCommand.Emergency.name -> R.drawable.emergency
            "SOS" -> R.drawable.emergency
            ActionInternalCommand.Telephone.name -> R.drawable.telephone
            ActionInternalCommand.Clock.name -> R.drawable.clock
            ActionInternalCommand.Contacts.name -> R.drawable.contacts
            ActionInternalCommand.Preferences.name -> R.drawable.preferences
            ActionInternalCommand.Settings.name -> R.drawable.settings
            ActionInternalCommand.SystemSettings.name -> R.drawable.settings_gear
            ActionInternalCommand.Messages.name -> R.drawable.messages
            ActionInternalCommand.Camera.name -> R.drawable.camera
            ActionInternalCommand.Gallery.name -> R.drawable.gallery
            ActionInternalCommand.GoToPage.name -> R.drawable.go_to_page
            ActionInternalCommand.OpenAPage.name -> R.drawable.open_a_page
            InternalCategories.SettingsSystem.name -> R.drawable.settings_gear
            InternalCategories.SettingsOverall.name -> R.drawable.settings
            else -> R.drawable.placeholder
        }
    }
}