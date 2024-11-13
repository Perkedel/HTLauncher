package com.perkedel.htlauncher.data

data class ActionData(// Items will do something out of these:
    val name:String = "Launch", // Name of this action
    val action:String = "", // Package name? exe name?
    val args:List<String>, // Arguments of it this
    val type:String = "", //Type of this action.

    /*
    * Types of Actions possible
    * - ShellOpen e.g. ShellOpen("~/Zenless.sh") basically POSIX commandline
    * - Activity e.g. Activity("com.Hoyoverse.Nap", "CustomTabActivity") basically launch MainActivity or whatever launch of this APK. No argument means assume launch
    * - LauncherActivity e.g. LauncherActivity("com.Hoyoverse.Nap") same as above but presumes LauncherActivity of whatever in this APK
    * */
)
