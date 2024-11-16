package com.perkedel.htlauncher.data

data class PageData( // And inside a page, there are:
    val items:List<String> = listOf(
        "Item",
    ), // Cells you can interact to make it do something
    val cellSize:Int = 125, // Grid Size to be handed over to Cell Size
    val isHome:Boolean = true, // if this page is considered default page
)
