package com.perkedel.htlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
//import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            HTLauncherTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = {
//                        TopAppBar(
//                            title = {
//                                Text("haha")
//                            }
//                        )
//                    }
//                ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//            Column {
//                Text("Haha")
//                Text("Hihi")
//            }
            HomeGreeting()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGreeting(){
    HTLauncherTheme {
//        Scaffold (
//            modifier = Modifier.fillMaxSize(),
//            topBar = {
//                TopAppBar(
//                    title = { Text("HAHA") },
//                    colors = topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer,
//                        titleContentColor = MaterialTheme.colorScheme.primary,
//                    ),
//                )
//            }
//        ) {
//            innerPadding -> Column(
//                modifier = Modifier.padding(innerPadding),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ){
//
//            }
////            NavHost(navController, startDestination = Profile, Modifier.padding(innerPadding)) {
////
////            }
//
//        }
        Navigation()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    HTLauncherTheme {
////        Greeting("Android")
//
//    }
    HomeGreeting()
}