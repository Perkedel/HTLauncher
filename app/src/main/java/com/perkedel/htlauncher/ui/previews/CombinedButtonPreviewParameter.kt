package com.perkedel.htlauncher.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.perkedel.htlauncher.enumerations.ButtonTypes

data class ButtonPreviewParams(
    val buttonType:ButtonTypes,
    val enabled:Boolean
)

class CombinedButtonPreviewParameter: PreviewParameterProvider<ButtonPreviewParams> {
    // https://proandroiddev.com/using-previewparameters-and-providing-composables-to-jetpack-compose-previews-5b1f5a8fe192
    override val values: Sequence<ButtonPreviewParams>
        get()= sequenceOf(
            ButtonPreviewParams(buttonType = ButtonTypes.DefaultButton, enabled = true),
            ButtonPreviewParams(buttonType = ButtonTypes.TextButton, enabled = true),
            ButtonPreviewParams(buttonType = ButtonTypes.OutlineButton, enabled = true),
            ButtonPreviewParams(buttonType = ButtonTypes.IconButton, enabled = true),
            ButtonPreviewParams(buttonType = ButtonTypes.DefaultButton, enabled = false),
            ButtonPreviewParams(buttonType = ButtonTypes.TextButton, enabled = false),
            ButtonPreviewParams(buttonType = ButtonTypes.OutlineButton, enabled = false),
            ButtonPreviewParams(buttonType = ButtonTypes.IconButton, enabled = false),
        )
}