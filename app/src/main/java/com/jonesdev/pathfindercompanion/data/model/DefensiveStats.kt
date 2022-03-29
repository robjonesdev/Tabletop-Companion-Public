package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "defensiveStats", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class DefensiveStats(
    @PrimaryKey var characterOwner: String = "",
    @ColumnInfo(name = "currentHealth") val currentHealth: Int = 0,
    @ColumnInfo(name = "maxHealth") var maxHealth: Int = 0,
    @ColumnInfo(name = "armorClass") var armorClass: Int = 0,
    @ColumnInfo(name = "characterSpeed") val characterSpeed: Int = 0,
    @ColumnInfo(name = "fortitudeSave") val fortitudeSave: Int = 0,
    @ColumnInfo(name = "reflexSave") val reflexSave: Int = 0,
    @ColumnInfo(name = "willSave") val willSave: Int = 0): Serializable