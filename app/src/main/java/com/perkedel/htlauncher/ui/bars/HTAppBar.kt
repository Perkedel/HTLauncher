@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.ui.bars

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.Screen
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTAppBar(
    currentScreen: String = Screen.HomeScreen.name,
//    textTitle:String = stringResource(currentScreen.title),
    textTitle:String = currentScreen,
    textDescription:String? = "",
    iconModel: Any? = null,
    icon: (@Composable () -> Unit)? = {
        iconModel?.let {
            AsyncImage(
                modifier = Modifier
                    .padding(8.dp)
                    .sizeIn(maxWidth = 48.dp, maxHeight = 48.dp)
                ,
                model = it,
                contentDescription = "",
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.mavrickle),
            )
        }
    },
    title: @Composable () -> Unit = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                it()
            }
            if(!textDescription.isNullOrEmpty()){
                Column {
                    Text(
                        modifier = Modifier.basicMarquee(

                        ),
                        text = textTitle
                    )
                    Text(
                        modifier = Modifier.basicMarquee(

                        ),
                        lineHeight = 8.sp,
                        text = textDescription,
                        maxLines = 1,
                        fontSize = 12.sp,
                    )
                }
            } else {
                Text(
                    modifier = Modifier.basicMarquee(

                    ),
                    text = textTitle
                )
            }
        }
    },
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    hideIt: Boolean = false,
    hideMenuButton: Boolean = true,
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
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    view: View = LocalView.current,
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
                    IconButton(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            },
            actions = {
                actions() // https://youtu.be/laL2lAts_Rc
                if (onMoreMenu != null && !hideMenuButton) {
                    IconButton(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            onMoreMenu()
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

//@PreviewFontScale
//@PreviewLightDark
//@PreviewScreenSizes
//@PreviewDynamicColors
//@Preview(showBackground = true)
@HTPreviewAnnotations
@Composable
fun HTAppBarPreview(){
    HTLauncherTheme {
        Scaffold(

            topBar = { HTAppBar(
                currentScreen = Screen.HomeScreen.name,
                iconModel = R.drawable.cube,
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