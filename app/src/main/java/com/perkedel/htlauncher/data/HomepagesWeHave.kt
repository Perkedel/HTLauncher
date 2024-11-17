package com.perkedel.htlauncher.data

data class HomepagesWeHave( // In a Home Button, we have:
    val pagesPath: List<String> = listOf(
        "Home",
        "Second",
        "Third",
    ), // Pages we have
    val setting:SettingData = SettingData()

    // Extras: e.g. Customizations
)
