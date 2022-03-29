package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.Skills

@Dao
interface SkillsDao {

    @Query("SELECT * FROM skills")
    fun getAll(): List<Skills>

    @Query("SELECT * FROM skills WHERE characterOwner LIKE :characterOwner")
    fun getByOwnerName(characterOwner: String): Skills?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg inventoryItems: Skills)

    @Query("DELETE FROM skills WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM skills")
    fun wipeTable()

    @Delete
    fun delete(inventoryItem: Skills)
}