@file:OptIn(ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.ui.bars

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.Screen
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTAppBar(
    currentScreen: Screen = Screen.HomeScreen,
    textTitle:String = stringResource(currentScreen.title),
    textDescription:String? = "",
    title: @Composable () -> Unit = {
        if(!textDescription.isNullOrEmpty()){
            Column {
                Text(
                    text = textTitle
                )
                Text(
                    modifier = Modifier.basicMarquee(

                    ),
                    text = textDescription,
                    fontSize = 12.sp,
                )
            }
        } else {
            Text(text = textTitle)
        }

    },
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    hideIt: Boolean = false,
    modifier: Modifier = Modifier,
    actions: @Composable() (RowScope.() -> Unit) = {},
    onMoreMenu: (() -> Unit)? = null,
    moreMenuIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Menu"
        )
    },
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors:TopAppBarColors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ),
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight
){
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#8
    if (hideIt) {
//        a

    } else {
        TopAppBar(
//            title = { Text(text = (if (title!!.isNotBlank()) title.toString() else stringResource(currentScreen.title))) },
//            title = { Text(text = (stringResource(currentScreen.title))) },
            title = title,
//            colors = topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                titleContentColor = MaterialTheme.colorScheme.primary,
//            ),
            colors = colors,
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            },
            actions = {
                actions() // https://youtu.be/laL2lAts_Rc
                if (onMoreMenu != null) {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        moreMenuIcon()
                    }
                }
            },
            windowInsets = windowInsets,
            scrollBehavior = scrollBehavior,
            expandedHeight = expandedHeight,
        )
    }
}

private fun setShowTopBar(into:Boolean = true,handover:()->Boolean){

}

@PreviewFontScale
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@Preview(showBackground = true)
@Composable
fun HTAppBarPreview(){
    HTLauncherTheme {
        Scaffold(

            topBar = { HTAppBar(
                currentScreen = Screen.HomeScreen,
                textTitle = "Hello",
                textDescription = "World",
                onMoreMenu = {},
                canNavigateBack = true,
                hideIt = false,
            ) }
        ) { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {

            }

        }
    }
}