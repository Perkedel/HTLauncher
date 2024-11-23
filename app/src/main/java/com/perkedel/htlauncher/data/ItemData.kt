package com.perkedel.htlauncher.data

import android.graphics.Color
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.serialization.Serializable

@Serializable
data class ItemData(
    val type:String = "Item",
    val name:String = "anItem", // Name of this item. Not showing
    var label:String = name, // Label that shows above the icon. If empty, showLabel shows name above.
    var aria:String = label, // Aria says to be read by A11y narrator system.
    var imagePath:String = name, // Icon image path
    val action:List<ActionData> = listOf(
        ActionData(),
    ), // What should this item do?

    // Customize
    var showLabel:Boolean = true, // Show the label
    var showWhichIcon:Int = 0, // 0 = App Icon, 1 = TV Banner
    var textFillColor: String = "Black", // Color say of this label text
    var textOutlineColor: String = "White", // Color say of this label text stroke
)
