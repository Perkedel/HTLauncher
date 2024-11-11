package com.perkedel.htlauncher.ui.bars

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.Screen
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTAppBar(
    currentScreen: Screen,
    title: @Composable () -> Unit = { Text(text = (stringResource(currentScreen.title))) },
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    hideIt: Boolean = false,
    modifier: Modifier = Modifier
){
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#8
    if (hideIt) {
//        a

    } else {
        TopAppBar(
//            title = { Text(text = (if (title!!.isNotBlank()) title.toString() else stringResource(currentScreen.title))) },
//            title = { Text(text = (stringResource(currentScreen.title))) },
            title = title,
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
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
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu"
                    )
                }
            }
        )
    }
}

private fun setShowTopBar(into:Boolean = true,handover:()->Boolean){

}

@Preview(showBackground = true)
@Composable
fun HTAppBarPreview(){
    HTLauncherTheme {
        Scaffold(

            topBar = { HTAppBar(
                currentScreen = Screen.HomeScreen,

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