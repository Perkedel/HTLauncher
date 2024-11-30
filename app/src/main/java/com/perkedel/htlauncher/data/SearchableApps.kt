package com.perkedel.htlauncher.data

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector

data class SearchableApps(
//    val itself:ApplicationInfo,
    val packageName:String = "com.example.app",
    val label:String = "Example",
//    val icon:Drawable,
){
    // https://youtu.be/CfL6Dl2_dAE
    // https://github.com/philipplackner/SearchFieldCompose
    // https://medium.com/@atharvapajgade/filter-sort-lazycolumn-items-in-jetpack-compose-b3380a5c69eb
    // https://tomas-repcik.medium.com/listing-all-installed-apps-in-android-13-via-packagemanager-3b04771dc73
    fun doesMatchSearchQuery(query:String = "", ignoreCase:Boolean = true): Boolean{
        val matchingCombinations:List<String> = listOf(
            packageName,
            label,
            packageName.first().toString(),
            label.first().toString(),
            "$packageName$label",
            "$packageName $label",
            "${packageName.first()} ${label.first()}",
            "${packageName.first()} $label",
            "$packageName ${label.first()}",
            "$label$packageName",
            "$label $packageName",
            "${label.first()} ${packageName.first()}",
            "${label.first()} $packageName",
            "$label ${packageName.first()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = ignoreCase)
        }
    }
}
