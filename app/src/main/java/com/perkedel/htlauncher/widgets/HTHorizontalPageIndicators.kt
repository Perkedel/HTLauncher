@file:OptIn(ExperimentalComposeUiApi::class)

package com.perkedel.htlauncher.widgets

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
//import org.checkerframework.common.subtyping.qual.Bottom
import kotlin.math.absoluteValue

@Composable
fun HTHorizontalPageIndicators(
    pageCount: Int = 1,
    currentPage: Int = 1,
    targetPage: Int = 0,
    currentPageOffsetFraction: Float = 1f,
    modifier: Modifier = Modifier,
    indicatorColor: Color = rememberColorScheme().primary,
    showText:Boolean = true,
    textStartFromNumberOne:Boolean = false,
    unselectedIndicatorSize: Dp = 8.dp,
    selectedIndicatorSize: Dp = 10.dp,
    indicatorCornerRadius: Dp = 2.dp,
    indicatorPadding: Dp = 2.dp,
    textSize: TextUnit = 10.sp,
    onSetPage: (Int) -> Unit = {},
    view: View = LocalView.current,
){
    // https://medium.com/@domen.lanisnik/exploring-the-official-pager-in-compose-8c2698c49a98 yoink!!
    // https://stackoverflow.com/a/75771001/9079640
    // https://developer.android.com/develop/ui/compose/touch-input/focus/change-focus-behavior

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .semantics {
                invisibleToUser()
            }
            .wrapContentSize()
//            .fillMaxWidth()
            .height(selectedIndicatorSize + indicatorPadding * 2)
    ) {
        // draw an indicator for each page
        repeat(pageCount) { page ->
            // calculate color and size of the indicator
            val (color, size) =
                if (currentPage == page || targetPage == page) {
                    // calculate page offset
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    // calculate offset percentage between 0.0 and 1.0
                    val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)

                    val size =
                        unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)

                    indicatorColor.copy(
                        alpha = offsetPercentage
                    ) to size
                } else {
                    indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
                }

//            val interactionSource = remember { MutableInteractionSource(
//
//            ) }

            // draw indicator
            Box(
                modifier = Modifier
                    .padding(
                        // apply horizontal padding, so that each indicator is same width
                        horizontal = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2,
                        vertical = size / 4
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .width(size)
                    .height(size / 2)
                    .focusProperties {
                        canFocus = false
                    }
                    .clickable(
                        onClick = {
                            onSetPage(page)
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                        },
                    )
                ,
                contentAlignment = Alignment.Center
            ){
                if(showText) {
                    Text(
                        modifier = Modifier.fillMaxHeight(),
                        textAlign = TextAlign.Center,
                        text = "${page + if(textStartFromNumberOne) 1 else 0}",
                        fontSize = textSize,
                        lineHeight = 1.sp,
                    )

                }
            }
        }
    }

}

@HTPreviewAnnotations
@Composable
fun HTHorizontalPageIndicatorsPreview(){
    HTLauncherTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .statusBarsPadding(),
            ) {
                HTHorizontalPageIndicators(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    pageCount = 10,
                    currentPage = 5,
                    targetPage = 5,
                    selectedIndicatorSize = 32.dp,
                    unselectedIndicatorSize = 18.dp,
                    indicatorCornerRadius = 24.dp,
                    textSize = 6.sp,
                    indicatorColor = rememberColorScheme().primary
                )
            }
        }
    }
}