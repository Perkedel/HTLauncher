package com.perkedel.htlauncher.enumerations

enum class ActionDataLaunchType(type:String = "") {
    LauncherActivity("LauncherActivity"),
    Activity(type = "Activity"),
    ShellOpen(type = "ShellOpen"),
}