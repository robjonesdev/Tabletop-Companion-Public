package com.jonesdev.pathfindercompanion.data.datasource.api

import com.google.gson.JsonObject
import com.jonesdev.pathfindercompanion.shared.Constants
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path

interface GoogleSheetsAPI {

    // spreadsheet key
    //102X1gXz6OffNoMaM5-KDcaK6hKeKaxBP0mLEWHBXAfQ

    // spreadsheet url
    //https://sheets.googleapis.com/v4/spreadsheets/?key=

    // access a specific character sheet
    //https://sheets.googleapis.com/v4/spreadsheets//values/Theodore?key=

    @GET("/v4/spreadsheets/?key=&fields=sheets.properties.title")
    suspend fun getCharacters(): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!A55:B62?key=")
    suspend fun getCharacterGeneralData(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!A44?key=")
    suspend fun getCharacterImage(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!A2:B8?key=")
    suspend fun getCharacterDefensiveStats(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!A11:B16?key=")
    suspend fun getCharacterAbilityModifiers(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!A19:C40?key=")
    suspend fun getCharacterPerceptionAndSkills(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!E3:J76?key=")
    suspend fun getCharacterInventory(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!L3:M21?key=")
    suspend fun getCharacterConditions(@Path("characterName") characterName: String): JsonObject

    @GET("/v4/spreadsheets//values/{characterName}!O3:U60?key=")
    suspend fun getCharacterSpellsAndAbilities(@Path("characterName") characterName: String): JsonObject
}