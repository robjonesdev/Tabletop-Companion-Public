package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.AbilityModifiers

@Dao
interface AbilityModifiersDao {

    @Query("SELECT * FROM abilityModifiers")
    fun getAll(): List<AbilityModifiers>

    @Query("SELECT * FROM abilityModifiers WHERE characterOwner LIKE :characterOwner")
    fun getByOwnerName(characterOwner: String): AbilityModifiers?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg abilityModifiers: AbilityModifiers)

    @Query("DELETE FROM abilityModifiers WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM abilityModifiers")
    fun wipeTable()

    @Delete
    fun delete(abilityModifiers: AbilityModifiers)
}