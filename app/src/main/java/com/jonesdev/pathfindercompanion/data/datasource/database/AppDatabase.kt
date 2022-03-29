package com.jonesdev.pathfindercompanion.data.datasource.database

import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import com.jonesdev.pathfindercompanion.data.datasource.database.dao.*
import com.jonesdev.pathfindercompanion.data.model.*

@Database(entities = [Character::class, Ability::class, AbilityModifiers::class, DefensiveStats::class, Skills::class, GeneralData::class, InventoryItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterSchema(): CharacterDao
    abstract fun abilitySchema(): AbilityDao
    abstract fun abilityModifiersSchema(): AbilityModifiersDao
    abstract fun defensiveStatsSchema(): DefensiveStatsDao
    abstract fun skillsSchema(): SkillsDao
    abstract fun generalDataSchema(): GeneralDataDao
    abstract fun inventoryItemSchema(): InventoryItemDao
}
