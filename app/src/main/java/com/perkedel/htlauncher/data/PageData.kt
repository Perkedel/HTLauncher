package com.perkedel.htlauncher.data

import com.perkedel.htlauncher.enumerations.PageGridType
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.enumerations.ShowWhichIcon
import kotlinx.serialization.Serializable

@Serializable
data class PageData( // And inside a page, there are:
    val type:String = "Page",
    val name:String = "anPage",
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
    val gridType: PageGridType = PageGridType.Default,
    val cellSize:Int = 125, // Grid Size to be handed over to Cell Size
    val cellCount:Int = 3, // Grid count for Fixed
    val cellCountLandscape:Int = 6, // Grid count for Fixed isCompact
    val showWhichIcon: ShowWhichIcon = ShowWhichIcon.Default,
    val isHome:Boolean = true, // if this page is considered default page
)
