package com.perkedel.htlauncher.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewParameter: PreviewParameterProvider<Boolean> {
    // https://youtu.be/nCd02GTBbIM?si=qPmiNj9oK_6AEUkO
    // https://developer.android.com/develop/ui/compose/tooling/previews#preview-data
    override val values: Sequence<Boolean>
        get() = sequenceOf(false,true)
}