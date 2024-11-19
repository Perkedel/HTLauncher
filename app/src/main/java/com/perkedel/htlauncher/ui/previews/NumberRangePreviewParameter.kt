package com.perkedel.htlauncher.ui.previews

import android.util.Range
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class NumberRangePreviewParameter (
    val min:Int = 0 ,
    val max:Int = Int.MAX_VALUE,
):PreviewParameterProvider<Int>

{
    // https://stackoverflow.com/questions/38481463/how-can-i-split-sequence-of-integer-into-ranges-java-kotlin
    // Google Search with Gemini: android studio range int to sequenceof
    // https://www.dhiwise.com/post/essential-insights-into-kotlin-range-syntax-and-functions
    // https://kotlinlang.org/docs/ranges.html
    // https://youtu.be/nCd02GTBbIM?si=qPmiNj9oK_6AEUkO
    // https://developer.android.com/develop/ui/compose/tooling/previews#preview-data

//    val min:Int = min
//    val max:Int = max

    override val values: Sequence<Int>
        get()= (min..max).asSequence()
}