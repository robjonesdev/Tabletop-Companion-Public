package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "abilities", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class Ability(
    @PrimaryKey(autoGenerate=true) var id: Int = 0,
    @ColumnInfo(name = "characterOwner") var characterOwner: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "cost") var cost: String = "",
    @ColumnInfo(name = "duration") var duration: String = "",
    @ColumnInfo(name = "range") var range: String = "",
    @ColumnInfo(name = "description")var description: String = "",
    @ColumnInfo(name = "level") var level: String = "",
    @ColumnInfo(name = "heighteningInfo")var heighteningInfo: String = ""
    ): Serializable