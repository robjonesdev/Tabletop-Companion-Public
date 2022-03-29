package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "generalData", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class GeneralData(
    @PrimaryKey var characterOwner: String = "",
    @ColumnInfo(name = "shortName") var shortName: String = "",
    @ColumnInfo(name = "longName") var longName: String = "",
    @ColumnInfo(name = "characterClass") var characterClass: String = "",
    @ColumnInfo(name = "race") var race: String = "",
    @ColumnInfo(name = "alignment") var alignment: String = "",
    @ColumnInfo(name = "deity") var deity: String = "",
    @ColumnInfo(name = "age") var age: String = "",
    @ColumnInfo(name = "notes") var notes: String = ""
): Serializable