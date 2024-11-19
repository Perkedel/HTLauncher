package com.perkedel.htlauncher.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.perkedel.htlauncher.enumerations.ButtonTypes

class ButtonTypePreviewParameter:PreviewParameterProvider<ButtonTypes> {
    override val values: Sequence<ButtonTypes>
        get() = sequenceOf(
            ButtonTypes.DefaultButton,
            ButtonTypes.OutlineButton,
            ButtonTypes.TextButton,
        )
}