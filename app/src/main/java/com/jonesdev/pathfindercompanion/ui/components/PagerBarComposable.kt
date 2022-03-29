package com.jonesdev.pathfindercompanion.ui.components

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jonesdev.pathfindercompanion.ui.TabPage

class PagerBarComposable {

    @Composable
    fun PagerBar(selectedTabIndex: Int, onSelectedTab:(TabPage) -> Unit){
        TabRow(selectedTabIndex = selectedTabIndex, backgroundColor = Color.Black) {
            TabPage.values().forEachIndexed { index, tabPage ->
                Tab(selected = index == selectedTabIndex,
                    onClick = {onSelectedTab(tabPage)},
                    text = { Text(text = tabPage.tabName) })
            }
        }
    }

}