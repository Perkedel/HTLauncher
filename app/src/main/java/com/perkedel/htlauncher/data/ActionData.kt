package com.perkedel.htlauncher.data

import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import kotlinx.serialization.Serializable

@Serializable
data class ActionData(// Items will do something out of these:
    val name:String = "Launch", // Name of this action
    val action:String = "", // Package name? exe name?
    val args:List<String> = listOf(
        "",
        "",
    ), // Arguments of it this
    val extras:(Map<String,String>)? = mapOf(
        Pair("AAAa", "BBB1"),
        Pair("AAAb", "BBB2"),
        Pair("AAAc", "BBB3"),
    ), // Extra arguments
    val type:ActionDataLaunchType = ActionDataLaunchType.LauncherActivity, //Type of this action.

    /*
    * Types of Actions possible
    * - ShellOpen e.g. ShellOpen("~/Zenless.sh") basically POSIX commandline
    * - Activity e.g. Activity("com.Hoyoverse.Nap", "CustomTabActivity") basically launch MainActivity or whatever launch of this APK. No argument means assume launch
    * - LauncherActivity e.g. LauncherActivity("com.Hoyoverse.Nap") same as above but presumes LauncherActivity of whatever in this APK
    * */
)
