package com.jonesdev.pathfindercompanion.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class SpellOrAbilityCard(val id: Int, val title: String, val spellOrAbility: Ability)