package com.jonesdev.pathfindercompanion.data.repository.api

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.jonesdev.pathfindercompanion.data.datasource.api.GoogleSheetsAPI
import com.jonesdev.pathfindercompanion.data.model.*
import com.jonesdev.pathfindercompanion.data.repository.CharacterDataRepository
import javax.inject.Inject

class GoogleSheetsDataRepositoryImpl @Inject constructor(private val api: GoogleSheetsAPI) :
    CharacterDataRepository {

    override suspend fun getCharacterNames(): List<String> {
        val jsonArray = api.getCharacters().get("sheets") as JsonArray?
        val characterList : MutableList<String> = mutableListOf()
        if (jsonArray != null) {
            for(entry: JsonElement in jsonArray){
                entry.asJsonObject?.get("properties")?.asJsonObject?.get("title")
                    ?.let { characterList.add(it.asString) }
            }
        }
        return characterList.toList()
    }

    override suspend fun getCharacterImageUrls(characters: List<String>): List<String> {
        val imageUrls: MutableList<String> = mutableListOf()
        for(character: String in characters){
            val characterImage: String? = api.getCharacterImage(character).get("values")?.asJsonArray?.get(0)?.asJsonArray?.get(0)?.asString
            if (characterImage != null) {
                imageUrls.add(characterImage)
            }
        }
        return imageUrls.toList()
    }

    override suspend fun getCharacterImageUrl(characterName: String): String {
        return api.getCharacterImage(characterName).get("values")?.asJsonArray?.get(0)?.asJsonArray?.get(0)?.asString ?: "https://www.seekpng.com/png/full/57-573238_vector-swords-crossed-sword-sword-icon.png"
    }

    override suspend fun getCharacterGeneralData(characterName: String): GeneralData {
        val apiReturn = api.getCharacterGeneralData(characterName).get("values")?.asJsonArray
        val image = getCharacterImageUrl(characterName)
        if (apiReturn != null) {
            return GeneralData(
                "",
                apiReturn.get(0).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(1).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(3).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(2).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(4).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(5).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(6).asJsonArray?.get(1)?.asString ?: "",
                apiReturn.get(7).asJsonArray?.get(1)?.asString ?: ""
            )
        }
        return GeneralData()
    }

    override suspend fun getCharacterDefensiveStats(characterName: String): DefensiveStats {
        val apiReturn = api.getCharacterDefensiveStats(characterName).get("values")?.asJsonArray
        if (apiReturn != null) {
            return DefensiveStats("",
                apiReturn.get(0).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(1).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(2).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(3).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(4).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(5).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(6).asJsonArray?.get(1)?.asInt ?: 0
            )
        }
        return DefensiveStats()
    }

    override suspend fun getCharacterAbilityModifiers(characterName: String): AbilityModifiers {
        val apiReturn = api.getCharacterAbilityModifiers(characterName).get("values")?.asJsonArray
        if (apiReturn != null) {
            return AbilityModifiers("",
                apiReturn.get(0).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(1).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(2).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(3).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(4).asJsonArray?.get(1)?.asInt ?: 0,
                apiReturn.get(5).asJsonArray?.get(1)?.asInt ?: 0
            )
        }
        return AbilityModifiers()
    }

    override suspend fun getCharacterPerceptionAndSkills(characterName: String): Skills {
        val apiReturn = api.getCharacterPerceptionAndSkills(characterName).get("values")?.asJsonArray ?: JsonArray()
        val perceptionAndSkills: MutableList<String> = mutableListOf()
        for (i in 0 until apiReturn.size()) {
            val skill = apiReturn.get(i).asJsonArray?.get(0)?.asString ?: ""
            val skillModifier = apiReturn.get(i)?.asJsonArray?.get(2)?.asString ?: ""
            if(skill!=""){
                val skillString = "$skill: $skillModifier"
                perceptionAndSkills.add(skillString)
            }
        }
        return Skills(characterName, perceptionAndSkills)
    }

    override suspend fun getCharacterInventory(characterName: String): List<InventoryItem>  {
        val apiReturn = api.getCharacterInventory(characterName).get("values")?.asJsonArray ?: JsonArray()
        val inventory: MutableList<InventoryItem> = mutableListOf()
        for(i in 0 until apiReturn.size()){
            try{
                val name = apiReturn.get(i).asJsonArray?.get(0)?.asString ?: ""
                val count =  apiReturn.get(i).asJsonArray?.get(1)?.asInt ?: 0
                val bulk = apiReturn.get(i).asJsonArray?.get(2)?.asFloat ?: 0f
                val price = apiReturn.get(i).asJsonArray?.get(4)?.asString ?: ""
                val desc = apiReturn.get(i).asJsonArray?.get(5)?.asString ?: ""
                val inventoryItem = InventoryItem(
                    0,
                    "",
                    name,
                    count,
                    bulk,
                    price,
                    desc)
                if(inventoryItem.name!=""){
                    inventory.add(inventoryItem)
                }
            }
            catch(e: java.lang.IndexOutOfBoundsException){
                // left blank, meant to catch empty rows
            }
            catch(e: java.lang.NumberFormatException){
                // left blank, meant to catch poorly formatted counts and bulks
            }
        }
        return inventory
    }

    override suspend fun getCharacterConditions(characterName: String): JsonObject {
        return api.getCharacterConditions(characterName)
    }

    override suspend fun getCharacterSpellsAndAbilities(characterName: String): List<Ability> {
        val apiReturn = api.getCharacterSpellsAndAbilities(characterName).get("values")?.asJsonArray ?: JsonArray()
        val spellsAndAbilities: MutableList<Ability> = mutableListOf()
        for(i in 0 until apiReturn.size()){
            try{
                val name = apiReturn.get(i).asJsonArray?.get(0)?.asString ?: ""
                val cost = apiReturn.get(i).asJsonArray?.get(1)?.asString ?: ""
                val duration = apiReturn.get(i).asJsonArray?.get(2)?.asString ?: ""
                val range = apiReturn.get(i).asJsonArray?.get(3)?.asString ?: ""
                val desc = apiReturn.get(i).asJsonArray?.get(4)?.asString ?: ""
                val level = apiReturn.get(i).asJsonArray?.get(5)?.asString ?: ""
                val heighteningInfo = apiReturn.get(i).asJsonArray?.get(6)?.asString ?: ""
                val abilityOrSpell = Ability(0,"",name, cost, duration, range, desc, level, heighteningInfo)
                if(abilityOrSpell.name!=""){
                    spellsAndAbilities.add(abilityOrSpell)
                }
            }
            catch(e: java.lang.IndexOutOfBoundsException){
                // left blank, meant to catch empty rows
            }
            catch(e: java.lang.NumberFormatException){
                // left blank, meant to catch poorly formatted counts and bulks
            }
        }
        return spellsAndAbilities
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
}