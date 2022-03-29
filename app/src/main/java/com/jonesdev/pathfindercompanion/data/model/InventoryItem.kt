package com.jonesdev.pathfindercompanion.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "inventoryItems", foreignKeys = [ForeignKey(entity = Character::class,
    parentColumns = arrayOf("characterName"),
    childColumns = arrayOf("characterOwner"),
    onDelete = ForeignKey.CASCADE)])
data class InventoryItem(
    @PrimaryKey(autoGenerate=true) var id: Int = 0,
    @ColumnInfo(name = "characterOwner") var characterOwner: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "count") var count: Int = 0,
    @ColumnInfo(name = "bulk") var bulk: Float = 0f,
    @ColumnInfo(name = "price") var price: String = "0cp",
    @ColumnInfo(name = "description") var description: String = ""
    ): Serializable