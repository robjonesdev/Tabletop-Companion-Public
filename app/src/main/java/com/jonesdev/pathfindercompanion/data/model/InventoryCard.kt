package com.jonesdev.pathfindercompanion.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class InventoryCard(val id: Int, val title: String, val inventoryItem: InventoryItem){

}