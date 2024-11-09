package com.perkedel.htlauncher.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
//    navController: NavController?,
    isFirstPage:Boolean = false,
    onAllAppButtonClicked: () -> Unit,
    onMoreMenuButtonClicked: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
    hideTopBar:Boolean = true,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        Button(
//            onClick = {
////                    navController?.navigate(Screen.AllAppsScreen)
////                navController?.navigate(Screen.AllAppsScreen.name)
//                onAllAppButtonClicked()
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(text="All Apps")
//        }
        BasePage(
            isFirstPage = true,
            onMoreMenuButtonClicked = onMoreMenuButtonClicked,
            modifier = Modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HTLauncherTheme {
        HomeScreen(
            isFirstPage = true,
            onAllAppButtonClicked = {},
            onMoreMenuButtonClicked = {},
        )
    }
}