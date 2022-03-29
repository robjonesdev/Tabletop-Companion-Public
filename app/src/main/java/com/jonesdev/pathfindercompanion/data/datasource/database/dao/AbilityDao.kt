package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.Ability

@Dao
interface AbilityDao {

    @Query("SELECT * FROM abilities")
    fun getAll(): List<Ability>

    @Query("SELECT * FROM abilities WHERE id IN (:idList)")
    fun loadAllByIds(idList: Array<Int>): List<Ability>

    @Query("SELECT * FROM abilities WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Ability

    @Query("SELECT * FROM abilities WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): Ability

    @Query("SELECT * FROM abilities WHERE characterOwner LIKE :characterOwner")
    fun getAllByOwnerName(characterOwner: String): List<Ability>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg inventoryItems: Ability)

    @Query("DELETE FROM abilities WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM abilities")
    fun wipeTable()

    @Delete
    fun delete(inventoryItem: Ability)
}