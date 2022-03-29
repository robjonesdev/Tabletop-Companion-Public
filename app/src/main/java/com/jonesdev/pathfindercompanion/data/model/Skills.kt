package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "skills", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class Skills(
    @PrimaryKey var characterOwner: String = "",
    @ColumnInfo(name = "skillList") var skillList: List<String> = emptyList()
): Serializable

