package com.perkedel.htlauncher

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import android.content.Context;
import androidx.compose.material3.Card
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.perkedel.htlauncher.ui.dialog.HomeMoreMenu
import com.perkedel.htlauncher.widgets.FirstPageCard
import com.perkedel.htlauncher.ui.navigation.HomeScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    anViewModel: HTViewModel = viewModel()
){
    // https://youtu.be/4gUeyNkGE3g
    // https://github.com/philipplackner/NavigationMultiModule
    // https://youtube.com/shorts/SAD8flVdILY?si=9L164ahugFtx0g-J
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.HomeScreen.name
    )
    val hideTopBar:Boolean = false
    val htuiState : HTUIState by anViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            HTAppBar(
                currentScreen = currentScreen,
                canNavigateBack =navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()},
//                hideIt = hideTopBar
                hideIt = navController.previousBackStackEntry == null
            )
        },

    ) { innerPadding ->
//        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen.name,
            modifier = Modifier.padding(innerPadding),
            ) {
            composable(route = Screen.HomeScreen.name) {
                HomeScreen(
//                    navController = navController,
                    onAllAppButtonClicked = {
                        navController.navigate(Screen.AllAppsScreen.name)
                    },
                    onMoreMenuButtonClicked = {
                        anViewModel.openTheMoreMenu(true)
                    }
                )
                if(htuiState.openMoreMenu){
                    HomeMoreMenu(
                        modifier = Modifier,
                        onChosenMenu = {
                            // https://stackoverflow.com/a/53138234
                            when(it){
                                0->{
                                    // Edit
                                }
                                1->{
                                    // Configurations
                                }
                                2->{
                                    // System Setting
                                }
                                3->{
                                    // All Apps
                                    navController.navigate(Screen.AllAppsScreen.name)
                                }
                                else->{}
                            }
                            anViewModel.openTheMoreMenu(false)
                        },
                        onDismissRequest = {anViewModel.openTheMoreMenu(false)}
                    )
                }
            }
            composable(
                route = Screen.AllAppsScreen.name,

                ) {
                AllAppsScreen(navController = navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTAppBar(
    currentScreen: Screen,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    hideIt:Boolean = false,
    modifier: Modifier = Modifier
){
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#8
    if (hideIt) {
//        a

    } else {
        TopAppBar(
            title = { Text(stringResource(currentScreen.title)) },
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
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        )
    }
}

private fun setShowTopBar(into:Boolean = true,handover:()->Boolean){

}

private fun getAllApps(){

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppsScreen(
    navController: NavController?,

){
    // https://stackoverflow.com/questions/64377518/how-to-initialize-or-access-packagemanager-out-from-coroutinecontext
    // https://www.geeksforgeeks.org/different-ways-to-get-list-of-all-apps-installed-in-your-android-phone/
    val context = LocalContext.current
//    val pm:PackageManager = getPackageManager()
    val pm:PackageManager = context.packageManager
//    val pm:PackageManager = context.getApp
    val packList = pm.getInstalledPackages(0)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        Text("HAI ALL")
//        items(5){
//            Text("HAH ${it}")
//        }
//        pm.getInstalledPackages(0)
        items(packList.size){
            Text("${packList.get(it).applicationInfo.loadLabel(pm)}")
        }
    }
}

