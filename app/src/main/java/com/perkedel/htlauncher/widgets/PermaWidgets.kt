package com.perkedel.htlauncher.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme

@Composable
fun FirstPageCard(
    modifier: Modifier,
    onMoreMenuButton: () -> Unit,
){
//    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Text("HAHAHA")
                Text("HAHAHA")
                Text("HAHAHA")
            }
            IconButton(
                onClick = onMoreMenuButton,
//                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstPageCardPreview(){
    HTLauncherTheme {
        FirstPageCard(
            modifier = Modifier,
            onMoreMenuButton = {},

        )
    }
}