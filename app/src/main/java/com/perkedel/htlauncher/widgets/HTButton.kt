@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.ui.previews.BooleanPreviewParameter
import com.perkedel.htlauncher.ui.previews.ButtonPreviewParams
import com.perkedel.htlauncher.ui.previews.ButtonTypePreviewParameter
import com.perkedel.htlauncher.ui.previews.CombinedButtonPreviewParameter
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotationsPOC
import com.perkedel.htlauncher.ui.previews.PreviewButtons
import com.perkedel.htlauncher.ui.theme.FocusColorDark
import com.perkedel.htlauncher.ui.theme.FocusColorDarkContainer
import com.perkedel.htlauncher.ui.theme.FocusColorLight
import com.perkedel.htlauncher.ui.theme.FocusColorLightContainer
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.UnfocusColorDark
import com.perkedel.htlauncher.ui.theme.UnfocusColorDarkContainer
import com.perkedel.htlauncher.ui.theme.UnfocusColorLight
import com.perkedel.htlauncher.ui.theme.UnfocusColorLightContainer
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun HTButton(
    modifier: Modifier = Modifier,
    buttonType:ButtonTypes = ButtonTypes.DefaultButton,
    /*
        * Button Types
        * 0. Default
        * 1. Outline
        * 3. Text
        * */
    leftIcon:ImageVector? = null,
    rightIcon:ImageVector? = null,
    leftIconDescriptor:String = "",
    rightIconDescriptor:String = "",
    title:String = "",

    leadingIcon: @Composable() (() -> Unit)? = {
        if(leftIcon != null) Icon(leftIcon,leftIconDescriptor)
    },
    trailingIcon: @Composable() (() -> Unit)? = {
        if(rightIcon != null) Icon(rightIcon,rightIconDescriptor)
    },

    enabled:Boolean = true,
    focusThisFirst:Boolean = false,
    shape: Shape = when(buttonType){
        ButtonTypes.OutlineButton -> ButtonDefaults.outlinedShape
        ButtonTypes.TextButton -> ButtonDefaults.textShape
        else -> ButtonDefaults.shape
    },
    iconButtonColors:IconButtonColors = IconButtonDefaults.iconButtonColors(),
    colors: ButtonColors = when(buttonType){
        ButtonTypes.OutlineButton -> ButtonDefaults.outlinedButtonColors()
        ButtonTypes.TextButton -> ButtonDefaults.textButtonColors()
        else -> ButtonDefaults.buttonColors(

        )
    },
    elevation: ButtonElevation? = when(buttonType){
        ButtonTypes.OutlineButton -> ButtonDefaults.buttonElevation()
        ButtonTypes.TextButton -> ButtonDefaults.buttonElevation()
        else -> ButtonDefaults.buttonElevation()
    },
    border: BorderStroke? = when(buttonType){
        ButtonTypes.OutlineButton -> ButtonDefaults.outlinedButtonBorder()
        ButtonTypes.TextButton -> null
        else -> null
    },
    contentPadding: PaddingValues = when(buttonType){
        ButtonTypes.OutlineButton -> ButtonDefaults.ContentPadding
        ButtonTypes.TextButton -> ButtonDefaults.TextButtonContentPadding
        else -> ButtonDefaults.ContentPadding
    },
    interactionSource: MutableInteractionSource? = null,

    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,

    onLongClickLabel:String? = null,

    view: View = LocalView.current,

    content: @Composable() (RowScope.() -> Unit) = {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Text(title)
        if (trailingIcon != null) {
            trailingIcon()
        }
    }
){
    // https://youtu.be/nCd02GTBbIM?si=ygaiFektH1vXio_B Philipp Lackner Button UI Preview
    // https://stackoverflow.com/a/65850523/9079640
    // https://proandroiddev.com/using-previewparameters-and-providing-composables-to-jetpack-compose-previews-5b1f5a8fe192
    // https://stackoverflow.com/a/76075029/9079640
//    val longClickModifier:Modifier = if (onLongClick != null) combinedClickable (
//        onClick = {},
//        onLongClick = onLongClick,
//    ) else null
    // https://stackoverflow.com/a/75701959/9079640
    // https://developer.android.com/develop/ui/compose/touch-input/focus/react-to-focus
    val darkCheck:Boolean = isSystemInDarkTheme()
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
    var focusButtonColor:ButtonColors = ButtonDefaults.buttonColors(
        containerColor = backgroundFocusColor
    )
    var focusIconButtonColors:IconButtonColors = IconButtonDefaults.iconButtonColors(
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

    val totalModifier:Modifier = focusAugmentModifier


    when(buttonType){
        ButtonTypes.OutlineButton -> OutlinedButton(
            modifier = totalModifier.combinedClickable(
                enabled = enabled,
                onClick = {},
                onLongClick = onLongClick
            ),
            enabled = enabled,
            shape = shape,
            colors = if(thereforeInFocus) focusButtonColor else colors,
            elevation = elevation,
            border = if (thereforeInFocus) BorderStroke(2.dp,borderFocusColor) else border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            content = content,
        )
        ButtonTypes.TextButton-> TextButton(
            modifier = totalModifier.combinedClickable(
                enabled = enabled,
                onClick = {},
                onLongClick = onLongClick
            ),
            enabled = enabled,
            shape = shape,
            colors = if(thereforeInFocus) focusButtonColor else colors,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            content = content,
        )
        ButtonTypes.IconButton -> IconButton(
            modifier = totalModifier.combinedClickable(
                enabled = enabled,
                onClick = {},
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
                onClickLabel = onLongClickLabel
            ),
            enabled = enabled,

            colors = if(thereforeInFocus) focusIconButtonColors else iconButtonColors,

            interactionSource = interactionSource,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            content = {
                if(leadingIcon != null){
                    leadingIcon()
                } else {
                    if(leftIcon != null) Icon(leftIcon,leftIconDescriptor) else {
                        Row{
                            content()
                        }
                    }
                }
            },
        )

        else -> Button(
            modifier = totalModifier.combinedClickable(
                enabled = enabled,
                onClick = {},
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
                onClickLabel = onLongClickLabel
            ),
            enabled = enabled,
            shape = shape,
            colors = if(thereforeInFocus) focusButtonColor else colors,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            content = content,
        )
    }
}

@PreviewButtons
//@HTPreviewAnnotations
//@Preview
@Composable
fun HTButtonPreview(
//    @PreviewParameter(NumberRangePreviewParameter::class,5) buttonType: Int
//    @PreviewParameter(ButtonTypePreviewParameter::class) buttonType:ButtonTypes,
//    @PreviewParameter(BooleanPreviewParameter::class) enabled:Boolean, // wtf not allowed?
    @PreviewParameter(CombinedButtonPreviewParameter::class) data: ButtonPreviewParams
){
    HTLauncherTheme {
        HTButton(
//            modifier = Modifier,
//            buttonType = 2,
//            buttonType = ButtonTypes.TextButton,
//            buttonType = buttonType,
            buttonType = data.buttonType,
//            enabled = enabled,
            enabled = data.enabled,
            title = "Hello Button",
            leftIcon = Icons.Default.Android,
            rightIcon = Icons.Default.ArrowCircleRight
        )
    }
}