@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun SinasamakiCounter(
    modifier: Modifier = Modifier,
    count: Int = 0,
    sayInstead: String = "",
    padding: Int = 2,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    forceSlideIncrease: Boolean = false,
    singleRoll: Boolean = false,
    flipRotation:Boolean = false,
    useSayInstead:Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
){
    // https://www.sinasamaki.com/animated-counter/
    // var count by remember { mutableStateOf(0) }
    var paddedCount = if(useSayInstead) sayInstead else count.toString().padStart(padding, '0')
    if(!singleRoll) {
        Row(
            modifier = Modifier
                .animateContentSize()
//            .padding(horizontal = 32.dp)
            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            paddedCount
                .mapIndexed { index, c -> Digit(c, count, index) }
                .forEach { digit ->
                    AnimatedContent(
                        targetState = digit,
                        transitionSpec = {
                            if ((targetState > initialState || forceSlideIncrease) && flipRotation) {
                                slideInVertically { -it } togetherWith slideOutVertically { it }
                            } else {
                                slideInVertically { it } togetherWith slideOutVertically { -it }
                            }
                        },
                        label = "Counter"
                    ) { digiton ->
                        Text(
                            "${digiton.digitChar}",
                            style = style,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
        }
    } else {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                if ((targetState > initialState || forceSlideIncrease) && flipRotation) {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                } else {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            }, label = "Counter"
        ) { counton ->
            Text(
                "${if(useSayInstead) sayInstead else counton}",
                style = style,
                textAlign = TextAlign.Center,
            )
        }
    }
}

data class Digit(val digitChar: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = digitChar.hashCode()
        result = 31 * result + fullNumber
        result = 31 * result + place
        return result
    }
}

operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}

@HTPreviewAnnotations
@Composable
fun SinasamakiCounterPreview(){
    HTLauncherTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                HTAppBar()
            }
        ){padding ->
            Box(
                modifier = Modifier.padding(padding),
                contentAlignment = Alignment.Center
            ){
                Card(
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                    ){
                        Row {
                            SinasamakiCounter(
                                count = 5
                            )
                        }

                    }

                }

            }
        }
    }
}