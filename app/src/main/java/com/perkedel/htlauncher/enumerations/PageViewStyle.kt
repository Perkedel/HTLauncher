package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes
import com.perkedel.htlauncher.R

enum class PageViewStyle(val type:Int = 0, @StringRes val label:Int = 0) {
    Default(type = 0, label = R.string.editor_page_view_style_default),
    Grid(type = 1, label = R.string.editor_page_view_style_grid),
    Column(type = 2, label = R.string.editor_page_view_style_column), // a.k.a. vertical list
}