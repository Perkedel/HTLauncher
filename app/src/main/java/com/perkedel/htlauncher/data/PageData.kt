package com.perkedel.htlauncher.data

import com.perkedel.htlauncher.enumerations.PageViewStyle
import kotlinx.serialization.Serializable

@Serializable
data class PageData( // And inside a page, there are:
    val type:String = "Page",
    val viewStyle: PageViewStyle = PageViewStyle.Default,
    val items:List<String> = listOf(
        "Item",
        "Item2",
        "Item3",
        "Item4",
        "Item5",
        "Item6",
        "Item7",
        "Item8",
        "Item9",
    ), // Cells you can interact to make it do something
    val cellSize:Int = 125, // Grid Size to be handed over to Cell Size
    val isHome:Boolean = true, // if this page is considered default page
)
