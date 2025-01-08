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
    val HOMESCREEN_FILE_DUMMY:HomepagesWeHave = HomepagesWeHave(
        pagesPath = listOf(
            "Dummy",
        )
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
    val HOMEPAGE_FILE_DUMMY:PageData = PageData(
        name = "Dummy",
        isHome = true,
        items = listOf(
            "GetStarted",
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
    val POC_APPS_PAGE:PageData = PageData(

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
    val GET_STARTED_FILE:ItemData = ItemData(
        name = "GetStarted",
        label = "GetStarted",
        aria = "GetStarted",
        action = listOf(
            ActionData(
                name = "GetStarted",
                action = "GetStarted",
                type = ActionDataLaunchType.Internal,
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
        var result:Int = R.drawable.placeholder
        try {
            if(whichIs != null){
                result = when{
                    whichIs == ActionInternalCommand.AllApps.name -> R.drawable.all_apps
                    whichIs == ActionInternalCommand.Emergency.name -> R.drawable.emergency
                    whichIs == "SOS" -> R.drawable.emergency
                    whichIs == ActionInternalCommand.Telephone.name -> R.drawable.telephone
                    whichIs == ActionInternalCommand.Clock.name -> R.drawable.clock
                    whichIs == ActionInternalCommand.Contacts.name -> R.drawable.contacts
                    whichIs == ActionInternalCommand.Preferences.name -> R.drawable.preferences
                    whichIs == ActionInternalCommand.Settings.name -> R.drawable.settings
                    whichIs == ActionInternalCommand.SystemSettings.name -> R.drawable.settings_gear
                    whichIs == ActionInternalCommand.Messages.name -> R.drawable.messages
                    whichIs == ActionInternalCommand.Camera.name -> R.drawable.camera
                    whichIs == ActionInternalCommand.Gallery.name -> R.drawable.gallery
                    whichIs == ActionInternalCommand.GoToPage.name -> R.drawable.go_to_page
                    whichIs == ActionInternalCommand.OpenAPage.name -> R.drawable.open_a_page
                    whichIs == InternalCategories.SettingsSystem.name -> R.drawable.settings_gear
                    whichIs == InternalCategories.SettingsOverall.name -> R.drawable.settings
                    whichIs.startsWith("Settings") -> ActionGoToSystemSetting.valueOf(whichIs.replaceFirst("Settings","")).icon
                    else -> R.drawable.placeholder
                }
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
        return result
    }
}