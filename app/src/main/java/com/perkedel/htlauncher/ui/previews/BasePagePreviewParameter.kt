package com.perkedel.htlauncher.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.perkedel.htlauncher.enumerations.PageGridType

data class PagePreviewParameter(
    val gridType: PageGridType = PageGridType.Default,
    val fixedCount: Int = 3,
    val adaptiveSize: Int = 128,
)

class BasePagePreviewParameter: PreviewParameterProvider<PagePreviewParameter> {
    override val values: Sequence<PagePreviewParameter>
        get() = sequenceOf(
            PagePreviewParameter(gridType = PageGridType.Default),
            PagePreviewParameter(gridType = PageGridType.Fixed),
            PagePreviewParameter(gridType = PageGridType.Adaptive),
            PagePreviewParameter(gridType = PageGridType.Fixed, fixedCount = 2),
            PagePreviewParameter(gridType = PageGridType.Fixed, fixedCount = 1),
            PagePreviewParameter(gridType = PageGridType.Fixed, fixedCount = 4),
            PagePreviewParameter(gridType = PageGridType.Fixed, fixedCount = 5),
        )
}