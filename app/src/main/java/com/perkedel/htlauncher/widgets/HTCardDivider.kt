package com.perkedel.htlauncher.widgets

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun HTCardDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 3.dp,
    color: Color = rememberColorScheme().background,
){
    // https://github.com/Kwasow/Musekit/blob/main/app/src/main/java/com/kwasow/musekit/ui/components/SettingsDivider.kt
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness,
    )
}