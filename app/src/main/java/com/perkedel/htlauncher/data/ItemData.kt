package com.perkedel.htlauncher.data

import android.graphics.Color
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

data class ItemData(
    val name:String = "anItem", // Name of this item. Not showing
    val label:String = name, // Label that shows above the icon. If empty, showLabel shows name above.
    val aria:String = label, // Aria says to be read by A11y narrator system.
    val action:List<ActionData> = listOf(
        ActionData(),
    ), // What should this item do?

    // Customize
    val showLabel:Boolean = true, // Show the label
    val showWhichIcon:Int = 0, // 0 = App Icon, 1 = TV Banner
    val textFillColor: String = "Black", // Color say of this label text
    val textOutlineColor: String = "White", // Color say of this label text stroke
)
