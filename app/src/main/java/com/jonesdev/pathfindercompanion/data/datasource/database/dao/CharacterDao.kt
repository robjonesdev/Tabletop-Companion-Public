package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): List<Character>

    @Query("SELECT * FROM character WHERE characterName IN (:nameList)")
    fun loadAllByIds(nameList: Array<String>): List<Character>

    @Query("SELECT * FROM character WHERE characterName LIKE :name LIMIT 1")
    fun findByName(name: String): Character

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg characters: Character)

    @Query("DELETE FROM character")
    fun wipeTable()

    @Delete
    fun delete(inventoryItem: Character)
}