package com.perkedel.htlauncher.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.FirstPageCard

@Composable
fun BasePage(
    isOnNumberWhat: Int = 0,
    isFirstPage: Boolean = false,
    howManyItemsHere: Int = 0,
    onMoreMenuButtonClicked: () -> Unit,
    modifier: Modifier.Companion,
){
    // https://youtu.be/UhnTTk3cwc4?si=5BoNxc4uZdM6y5nG
    // https://youtu.be/qP-ieASbqMY?si=JFoxgnsQyDf3iJob
    // https://youtu.be/NPmgnGFzopA?si=yOJydgvsQrLfsHKk
    // https://youtu.be/HmXgVBys7BU?si=u6nsssd2LeP48TED
    val lazyListState = rememberLazyGridState()
    Column(
        modifier.fillMaxWidth()
    ) {
        val windowInfo = rememberWindowInfo()
        val isCompact = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact
        if(isCompact){
            // if screen is compact
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                state = lazyListState,
                content = {
                    // Permanent Card on first page
                    if (isFirstPage){
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ){
                            FirstPageCard(
                                isCompact = isCompact,
                                isOnNumberWhat = isOnNumberWhat,
                                modifier = Modifier.weight(1f),
                                onMoreMenuButton = onMoreMenuButtonClicked,
                            )
                        }
                    }
                    // Rest of the items
                    items(howManyItemsHere){i->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.Blue),

                            ){
                            Text(text = "item $i")
                        }
                    }

                }
            )
        } else {
            // anything else
            Row {
                // Permanent Card on first page
                if(isFirstPage){
                    FirstPageCard(
                        isCompact = isCompact,
                        isOnNumberWhat = isOnNumberWhat,
                        modifier = Modifier,
                        onMoreMenuButton = onMoreMenuButtonClicked,
                    )
                }
                LazyVerticalGrid(
                    GridCells.Adaptive(100.dp),
                    state = lazyListState,
                    content = {
                        // Rest of the items
                        items(howManyItemsHere){i->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color.Blue),

                                ){
                                Text(text = "item $i")
                            }
                        }
                    }
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun BasePagePreview(){
    HTLauncherTheme {
        BasePage(
            isFirstPage = true,
            isOnNumberWhat = 0,
            howManyItemsHere = 10,
            onMoreMenuButtonClicked = {},
            modifier = Modifier
        )
    }
}