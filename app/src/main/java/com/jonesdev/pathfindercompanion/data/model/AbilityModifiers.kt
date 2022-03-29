package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "abilityModifiers", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class AbilityModifiers(
    @PrimaryKey var characterOwner: String = "",
    @ColumnInfo(name = "strengthModifier") val strengthModifier: Int = 0,
    @ColumnInfo(name = "dexterityModifier") val dexterityModifier: Int = 0,
    @ColumnInfo(name = "constitutionModifier") val constitutionModifier: Int = 0,
    @ColumnInfo(name = "intModifier") val intModifier: Int = 0,
    @ColumnInfo(name = "wisdomModifier") val wisdomModifier: Int = 0,
    @ColumnInfo(name = "charismaModifier") val charismaModifier: Int = 0): Serializable