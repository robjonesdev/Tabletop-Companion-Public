package com.jonesdev.pathfindercompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonesdev.pathfindercompanion.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.jonesdev.pathfindercompanion.data.repository.CharacterDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpellOrAbilityCardViewModel @Inject constructor(
    private val repository: CharacterDataRepository
): ViewModel() {

    private val _cards = MutableStateFlow(listOf<SpellOrAbilityCard>())
    val cards: StateFlow<List<SpellOrAbilityCard>> get() = _cards

    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

    fun getSpellOrAbilityCardsForCharacter(characterName: String){
        viewModelScope.launch(Dispatchers.Default) {
            val spellOrAbilityList = repository.getCharacterSpellsAndAbilities(characterName)
            val spellOrAbilityCardList = arrayListOf<SpellOrAbilityCard>()
            repeat(spellOrAbilityList.size) {
                val spellOrAbility = Ability(
                    0,
                    "",
                    spellOrAbilityList[it].name,
                    spellOrAbilityList[it].cost,
                    spellOrAbilityList[it].duration,
                    spellOrAbilityList[it].range,
                    spellOrAbilityList[it].description,
                    spellOrAbilityList[it].level,
                    spellOrAbilityList[it].heighteningInfo
                )
                spellOrAbilityCardList += SpellOrAbilityCard(
                    id = it,
                    spellOrAbility.name,
                    spellOrAbility
            )}
            _cards.emit(spellOrAbilityCardList)
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}