package com.perkedel.htlauncher.data

data class PageData( // And inside a page, there are:
    val items:List<String>, // Cells you can interact to make it do something
    val cellSize:Int, // Grid Size to be handed over to Cell Size
    val isHome:Boolean, // if this page is considered default page
)
