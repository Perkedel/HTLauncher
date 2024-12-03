package com.perkedel.htlauncher.enumerations

enum class ActionDataLaunchType(val type:String = "") {
    LauncherActivity(type = "LauncherActivity"),
    Activity(type = "Activity"),
    ShellOpen(type = "ShellOpen"),
    PageDialog(type = "PageDialog"), // Open a page in a dialog
    GoToPage(type = "GoToPage"), // Scroll your homescreen to page filename existed where in an order
    Glance(type = "Glance"), // The item is a homescreen widget
    Internal(type = "Internal"), // Internal command for this app.
    Category(type = "Category"), // turn the item into Category bar
}