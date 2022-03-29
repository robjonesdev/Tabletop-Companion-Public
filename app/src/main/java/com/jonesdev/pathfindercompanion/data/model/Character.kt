package com.jonesdev.pathfindercompanion.data.model

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "character")
data class Character(
    @PrimaryKey var characterName: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @Ignore var generalData: GeneralData = GeneralData(),
    @Ignore var defensiveStats: DefensiveStats = DefensiveStats(),
    @Ignore var abilityModifiers: AbilityModifiers = AbilityModifiers(),
    @Ignore var skills: Skills = Skills(),
    @Ignore var inventory: List<InventoryItem> = emptyList(),
    @Ignore var spellsAndAbilities: List<Ability> = emptyList()
): Serializable