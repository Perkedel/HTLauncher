package com.perkedel.htlauncher.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.perkedel.htlauncher.enumerations.ThirdButtonPosition

data class DialogPreviewKind(
    val hideIcon: Boolean = false,
    val confirmButton:Boolean = true,
    val dismissButton:Boolean = true,
    val thirdButton:Boolean = true,
    val thirdButtonPosition: ThirdButtonPosition = ThirdButtonPosition.Middle,
    val fancyButton: Boolean = false,
)

class DialogPreviewParameter: PreviewParameterProvider<DialogPreviewKind> {
    override val values: Sequence<DialogPreviewKind>
        get() = sequenceOf(
            DialogPreviewKind(hideIcon = false),
            DialogPreviewKind(hideIcon = true),
            DialogPreviewKind(confirmButton = true, dismissButton = true, thirdButton = true),
            DialogPreviewKind(confirmButton = true, dismissButton = true, thirdButton = false),
            DialogPreviewKind(confirmButton = true, dismissButton = false, thirdButton = true),
            DialogPreviewKind(confirmButton = true, dismissButton = false, thirdButton = false),
            DialogPreviewKind(confirmButton = false, dismissButton = true, thirdButton = true),
            DialogPreviewKind(confirmButton = false, dismissButton = true, thirdButton = false),
            DialogPreviewKind(confirmButton = false, dismissButton = false, thirdButton = true),
            DialogPreviewKind(confirmButton = false, dismissButton = false, thirdButton = false),
            DialogPreviewKind(thirdButtonPosition = ThirdButtonPosition.Left),
            DialogPreviewKind(thirdButtonPosition = ThirdButtonPosition.Middle),
            DialogPreviewKind(thirdButtonPosition = ThirdButtonPosition.Right),
            DialogPreviewKind(confirmButton = true, dismissButton = true, thirdButton = true, fancyButton = true),
            DialogPreviewKind(confirmButton = true, dismissButton = false, thirdButton = true, fancyButton = true),
            DialogPreviewKind(confirmButton = false, dismissButton = true, thirdButton = true, fancyButton = true),
            DialogPreviewKind(confirmButton = false, dismissButton = false, thirdButton = true, fancyButton = true),
        )
}