package com.jonesdev.pathfindercompanion.data.repository.database

import com.google.gson.JsonObject
import com.jonesdev.pathfindercompanion.data.datasource.database.AppDatabase
import com.jonesdev.pathfindercompanion.data.model.*
import com.jonesdev.pathfindercompanion.data.repository.CharacterDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomDatabaseRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase): CharacterDataRepository {

    // we want to keep the db from performing multiple transactions at once
    private var isPerformingTransaction = false;

    // TODO: implement transaction queue

    override suspend fun getCharacterNames(): List<String> {
        val nameList: MutableList<String> = mutableListOf()
        for(character: Character in appDatabase.characterSchema().getAll()){
            nameList.add(character.characterName)
        }
        return nameList
    }

    override suspend fun getCharacterImageUrls(characters: List<String>): List<String> {
        val imageUrlList: MutableList<String> = mutableListOf()
        for(character: Character in appDatabase.characterSchema().getAll()){
            imageUrlList.add(character.image)
        }
        return imageUrlList
    }

    override suspend fun getCharacterImageUrl(characterName: String): String {
        return appDatabase.characterSchema().findByName(characterName).image.toString()
    }

    override suspend fun getCharacterGeneralData(characterName: String): GeneralData {
        return appDatabase.generalDataSchema().getByOwnerName(characterName) ?: GeneralData()
    }

    override suspend fun getCharacterDefensiveStats(characterName: String): DefensiveStats {
        return appDatabase.defensiveStatsSchema().getByOwnerName(characterName) ?: DefensiveStats()
    }

    override suspend fun getCharacterAbilityModifiers(characterName: String): AbilityModifiers {
        return appDatabase.abilityModifiersSchema().getByOwnerName(characterName) ?: AbilityModifiers()
    }

    override suspend fun getCharacterPerceptionAndSkills(characterName: String): Skills {
        return appDatabase.skillsSchema().getByOwnerName(characterName) ?: Skills()
    }

    override suspend fun getCharacterInventory(characterName: String): List<InventoryItem> {
        return appDatabase.inventoryItemSchema().getAllByOwnerName(characterName) ?: emptyList()
    }

    override suspend fun getCharacterConditions(characterName: String): JsonObject {
        // TODO :: NYI
        return JsonObject()
    }

    override suspend fun getCharacterSpellsAndAbilities(characterName: String): List<Ability> {
        return appDatabase.abilitySchema().getAllByOwnerName(characterName) ?: emptyList()
    }

    override suspend fun getCompleteCharacterData(characterName: String): Character {
        return Character(characterName,
            getCharacterImageUrl(characterName),
            getCharacterGeneralData(characterName),
            getCharacterDefensiveStats(characterName),
            getCharacterAbilityModifiers(characterName),
            getCharacterPerceptionAndSkills(characterName),
            getCharacterInventory(characterName)
        )
    }

    override suspend fun getAllCompleteCharacterData(): List<Character> {
        val characterNameList = getCharacterNames()
        val characterList : MutableList<Character> = mutableListOf()
        for(characterName in characterNameList){
            characterList.add(getCompleteCharacterData(characterName))
        }
        return characterList
    }

    suspend fun updateStoredCharacterData(characters: List<Character>) = withContext(Dispatchers.IO) {
        if(!isPerformingTransaction){
            isPerformingTransaction = true
            for(character: Character in characters) appDatabase.let {
                it.characterSchema().delete(character)
                it.characterSchema().insertAll(character)

                character.generalData.characterOwner = character.characterName
                it.generalDataSchema().deleteForOwner(character.characterName)
                it.generalDataSchema().insertAll(character.generalData)

                character.defensiveStats.characterOwner = character.characterName
                it.defensiveStatsSchema().deleteForOwner(character.characterName)
                it.defensiveStatsSchema().insertAll(character.defensiveStats)

                character.abilityModifiers.characterOwner = character.characterName
                it.abilityModifiersSchema().deleteForOwner(character.characterName)
                it.abilityModifiersSchema().insertAll(character.abilityModifiers)

                character.skills.characterOwner = character.characterName
                it.skillsSchema().deleteForOwner(character.characterName)
                it.skillsSchema().insertAll(character.skills)

                it.inventoryItemSchema().deleteForOwner(character.characterName)
                for(inventoryItem: InventoryItem in character.inventory){
                    inventoryItem.characterOwner = character.characterName
                    it.inventoryItemSchema().insertAll(inventoryItem)
                }

                it.abilitySchema().deleteForOwner(character.characterName)
                for(ability: Ability in character.spellsAndAbilities){
                    ability.characterOwner = character.characterName
                    it.abilitySchema().insertAll(ability)
                }
            }
            // transaction complete, return us to a changeable state
            isPerformingTransaction = false;
        }
    }
}