package com.jonesdev.pathfindercompanion.data.repository

import com.google.gson.JsonObject
import com.jonesdev.pathfindercompanion.data.model.*

// Made as an interface for testing purposes, such that a testing implementation
// can override the functionality to return fake data and not use network resources
// or quota amounts
interface CharacterDataRepository {

    suspend fun getCharacterNames(): List<String>

    suspend fun getCharacterImageUrls(characters: List<String>): List<String>

    suspend fun getCharacterImageUrl(characterName: String): String?

    suspend fun getCharacterGeneralData(characterName:String): GeneralData

    suspend fun getCharacterDefensiveStats(characterName: String): DefensiveStats

    suspend fun getCharacterAbilityModifiers(characterName: String): AbilityModifiers

    suspend fun getCharacterPerceptionAndSkills(characterName: String): Skills

    suspend fun getCharacterInventory(characterName: String): List<InventoryItem>

    suspend fun getCharacterConditions(characterName: String): JsonObject

    suspend fun getCharacterSpellsAndAbilities(characterName: String): List<Ability>

    suspend fun getCompleteCharacterData(characterName: String): Character

    suspend fun getAllCompleteCharacterData(): List<Character>

}