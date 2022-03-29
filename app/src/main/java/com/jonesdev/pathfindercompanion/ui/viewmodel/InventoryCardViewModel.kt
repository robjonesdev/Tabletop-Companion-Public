package com.jonesdev.pathfindercompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonesdev.pathfindercompanion.data.model.InventoryCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.jonesdev.pathfindercompanion.data.model.InventoryItem
import com.jonesdev.pathfindercompanion.data.repository.CharacterDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryCardViewModel @Inject constructor(
    private val repository: CharacterDataRepository
): ViewModel() {

    private val _cards = MutableStateFlow(listOf<InventoryCard>())
    val cards: StateFlow<List<InventoryCard>> get() = _cards

    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

    fun getInventoryCardsForCharacter(characterName: String){
        viewModelScope.launch(Dispatchers.Default) {
            val inventoryList = repository.getCharacterInventory(characterName)
            val inventoryCardList = arrayListOf<InventoryCard>()
            repeat(inventoryList.size) {
                val invItem = InventoryItem(
                    0,
                    "",
                    inventoryList[it].name,
                    inventoryList[it].count,
                    inventoryList[it].bulk,
                    inventoryList[it].price,
                    inventoryList[it].description
                )
                inventoryCardList += InventoryCard(
                id = it,
                title = invItem.name,
                inventoryItem = invItem
            )}
            _cards.emit(inventoryCardList)
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}