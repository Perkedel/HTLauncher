package com.perkedel.htlauncher.data.hardcodes

import androidx.datastore.preferences.protobuf.MapEntryLite
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType

object HTLauncherHardcodes {
    // https://stackoverflow.com/a/50520935/9079640
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
}