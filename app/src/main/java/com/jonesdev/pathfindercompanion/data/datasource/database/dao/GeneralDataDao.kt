package com.jonesdev.pathfindercompanion.data.datasource.database.dao

import androidx.room.*
import com.jonesdev.pathfindercompanion.data.model.GeneralData

@Dao
interface GeneralDataDao {

    @Query("SELECT * FROM generalData")
    fun getAll(): List<GeneralData>

    @Query("SELECT * FROM generalData WHERE characterOwner LIKE :characterOwner")
    fun getByOwnerName(characterOwner: String): GeneralData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg generalData: GeneralData)

    @Query("DELETE FROM generalData WHERE characterOwner LIKE :characterOwner")
    fun deleteForOwner(characterOwner: String)

    @Query("DELETE FROM generalData")
    fun wipeTable()

    @Delete
    fun delete(generalData: GeneralData)
}