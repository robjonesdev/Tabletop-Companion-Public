package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.DefensiveStats

@Dao
interface DefensiveStatsDao {

    @Query("SELECT * FROM defensiveStats")
    fun getAll(): List<DefensiveStats>

    @Query("SELECT * FROM defensiveStats WHERE characterOwner LIKE :characterOwner")
    fun getByOwnerName(characterOwner: String): DefensiveStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg inventoryItems: DefensiveStats)

    @Query("DELETE FROM defensiveStats WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM defensiveStats")
    fun wipeTable()

    @Delete
    fun delete(inventoryItem: DefensiveStats)
}