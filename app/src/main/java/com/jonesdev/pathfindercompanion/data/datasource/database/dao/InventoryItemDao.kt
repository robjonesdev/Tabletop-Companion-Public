package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.InventoryItem

@Dao
interface InventoryItemDao {

    @Query("SELECT * FROM inventoryItems")
    fun getAll(): List<InventoryItem>

    @Query("SELECT * FROM inventoryItems WHERE id IN (:idList)")
    fun loadAllByIds(idList: Array<Int>): List<InventoryItem>

    @Query("SELECT * FROM inventoryItems WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): InventoryItem

    @Query("SELECT * FROM inventoryItems WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): InventoryItem

    @Query("SELECT * FROM inventoryItems WHERE characterOwner LIKE :characterOwner")
    fun getAllByOwnerName(characterOwner: String): List<InventoryItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg inventoryItems: InventoryItem)

    @Query("DELETE FROM inventoryItems WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM inventoryItems")
    fun wipeTable()

    @Delete
    fun delete(inventoryItem: InventoryItem)
}