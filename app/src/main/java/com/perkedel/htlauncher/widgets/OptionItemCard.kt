@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakInterrupt
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.FocusColorDark
import com.perkedel.htlauncher.ui.theme.FocusColorDarkContainer
import com.perkedel.htlauncher.ui.theme.FocusColorLight
import com.perkedel.htlauncher.ui.theme.FocusColorLightContainer
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.UnfocusColorDark
import com.perkedel.htlauncher.ui.theme.UnfocusColorDarkContainer
import com.perkedel.htlauncher.ui.theme.UnfocusColorLight
import com.perkedel.htlauncher.ui.theme.UnfocusColorLightContainer

@Composable
fun OptionItemCard(
    context: Context = LocalContext.current,
    title: String = "OPTION",
    summary: String = "OPTION SUMMARY",
    icon: ImageVector? = null,
    iconModel: Any? = null,
    compactDesign:Boolean = false,
    fullSpanDesign:Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    focusThisFirst:Boolean = false,
    readAriaOnLongClick: Boolean = false,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    transparentContainer:Boolean = false,
    useCustomContent:Boolean = false,
    content: (@Composable() () -> Unit)? = null,
) {
    // https://developer.android.com/develop/ui/compose/components/card
    val darkCheck:Boolean = isSystemInDarkTheme()
    val titleTextSize:TextUnit = if(compactDesign) 16.sp else 24.sp
    val summaryTextSize:TextUnit = if(compactDesign) 12.sp else 16.sp
    var backgroundFocusColor by remember { mutableStateOf(
        if(darkCheck){
            FocusColorDarkContainer
        } else {
            FocusColorLightContainer
        }
    ) }
    var borderFocusColor by remember { mutableStateOf(
        if(darkCheck){
            FocusColorDark
        } else {
            FocusColorLight
        }
    ) }
    var thereforeInFocus:Boolean by remember { mutableStateOf(false) }
    var focusButtonColor: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = backgroundFocusColor
    )
    var focusIconButtonColors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = backgroundFocusColor
    )
    fun applyFocusColors(focusState: FocusState) {
        borderFocusColor = if (focusState.isFocused) {
            if(darkCheck){
                FocusColorDark
            } else {
                FocusColorLight
            }
        } else {
            if(darkCheck){
                UnfocusColorDark
            } else {
                UnfocusColorLight
            }
        }
        backgroundFocusColor = if (focusState.isFocused) {
            if(darkCheck){
                FocusColorDarkContainer
            } else {
                FocusColorLightContainer
            }
        } else {
            if(darkCheck){
                UnfocusColorDarkContainer
            } else {
                UnfocusColorLightContainer
            }
        }
        thereforeInFocus = focusState.isFocused
    }
    val focusAugmentModifier:Modifier = if (focusThisFirst) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        modifier
            .onFocusEvent(::applyFocusColors)
            .focusRequester(focusRequester)
            .focusable()
    }else {
        modifier
            .onFocusEvent(::applyFocusColors)
            .focusable()
    }

    val totalModifier = focusAugmentModifier
        .padding(if (fullSpanDesign) 2.dp else 4.dp)
        .combinedClickable(
            onClick = onClick,
            onLongClick = {
                val readout: String = "$title: $summary"
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                Toast
                    .makeText(
                        context,
                        readout,
                        Toast.LENGTH_SHORT
                    )
                    .show()
                if (readAriaOnLongClick) {
                    ttsSpeakInterrupt(
                        handover = tts,
                        message = readout
                    )
                }
                onLongClick()
            }
        )
    val liteModifier:Modifier = modifier
        .padding(if (fullSpanDesign) 2.dp else 4.dp)

    if(useCustomContent && content != null) {
        Card(
            modifier = liteModifier,
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = if (transparentContainer) Color.Transparent else Color.Unspecified
            ),
            elevation = CardDefaults.cardElevation(),
        ) {
            content()
        }

    } else {
        Card(
            modifier = totalModifier,
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = if (transparentContainer) Color.Transparent else Color.Unspecified
            ),
            elevation = CardDefaults.cardElevation(),
        ) {
            if (fullSpanDesign) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (compactDesign) {
                        Icon(
                            imageVector = icon ?: Icons.Default.Circle,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        AsyncImage(
                            model = iconModel,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp),
                            placeholder = painterResource(R.drawable.placeholder),
                            error = painterResource(R.drawable.mavrickle)
                        )
                    }
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        OutlinedText(
                            fontSize = titleTextSize,
                            text = title,
                            outlineDrawStyle = Stroke(
                                width = 1f,
                                miter = 5f,
                                join = StrokeJoin.Round,
                            ),
                            modifier = Modifier
                                .basicMarquee(),
                        )
                        Text(
                            text = summary,
                            modifier = Modifier
                                .basicMarquee(),
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.aspectRatio(1f),
//            contentAlignment = Alignment.Center,
                ) {
                    if (compactDesign) {
                        Icon(
                            imageVector = icon ?: Icons.Default.Circle,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        AsyncImage(
                            model = iconModel,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(48.dp),
                            placeholder = painterResource(R.drawable.placeholder),
                            error = painterResource(R.drawable.mavrickle)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedText(
                            fontSize = titleTextSize,
                            text = title,
                            outlineDrawStyle = Stroke(
                                width = 1f,
                                miter = 5f,
                                join = StrokeJoin.Round,
                            ),
                            modifier = Modifier
                                .basicMarquee(),
                        )
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = summary,
                            modifier = Modifier
                                .basicMarquee(),
                        )
                    }
                }
            }

        }
    }
}

@HTPreviewAnnotations
@Composable
fun OptionItemCardPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
            ) {
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ){
                    OptionItemCard(
                        fullSpanDesign = true,
                    )
                }
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ){
                    OptionItemCard(
                        fullSpanDesign = true,
                        compactDesign = true,
                    )
                }
                repeat(2){
                    item{
                        OptionItemCard()
                    }
                }
                repeat(2){
                    item{
                        OptionItemCard(
                            transparentContainer = true
                        )
                    }
                }
                repeat(2){
                    item{
                        OptionItemCard(
                            transparentContainer = true,
                            compactDesign = true,
                        )
                    }
                }
            }

        }
    }
}