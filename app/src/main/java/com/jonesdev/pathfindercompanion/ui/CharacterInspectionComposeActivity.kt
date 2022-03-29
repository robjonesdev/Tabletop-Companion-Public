package com.jonesdev.pathfindercompanion.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonesdev.pathfindercompanion.ui.theme.PathfinderCompanionTheme
import androidx.compose.ui.draw.rotate
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.jonesdev.pathfindercompanion.R
import com.jonesdev.pathfindercompanion.ui.components.InventoryCardComposable
import com.jonesdev.pathfindercompanion.ui.components.PagerBarComposable
import com.jonesdev.pathfindercompanion.ui.components.SpellOrAbilityCardComposable
import com.jonesdev.pathfindercompanion.ui.viewmodel.CharacterInspectionViewModel
import com.jonesdev.pathfindercompanion.ui.viewmodel.InventoryCardViewModel
import com.jonesdev.pathfindercompanion.ui.viewmodel.SpellOrAbilityCardViewModel
import com.jonesdev.pathfindercompanion.data.model.Character
import dagger.hilt.android.AndroidEntryPoint

enum class TabPage(val tabName: String){
    StatsAndSkillsPanel("Stats"),
    InventoryPanel("Inv"),
    SpellsAndAbilitiesPanel("Abilities")
}

@AndroidEntryPoint
class CharacterInspectionComposeActivity : ComponentActivity() {
    private val inventoryCardsViewModel by viewModels<InventoryCardViewModel>()
    private val spellOrAbilityCardsViewModel by viewModels<SpellOrAbilityCardViewModel>()
    private val characterInspectViewModel by viewModels<CharacterInspectionViewModel>()

    var pagerBarComposable = PagerBarComposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val character: Character? =
            intent.getSerializableExtra(R.string.character_pane_data.toString()) as? Character
        if(character!=null){
            showLoadingContent()
            // prep viewmodels
            inventoryCardsViewModel.getInventoryCardsForCharacter(character.characterName)
            spellOrAbilityCardsViewModel.getSpellOrAbilityCardsForCharacter(character.characterName)
            characterInspectViewModel.refreshCharacterData(character.characterName, false)
            // observe character data
            characterInspectViewModel.getCharacter().observe(this) { characterData ->
                setPanelContent(characterData)
            }
        }
    }

    private fun showLoadingContent(){
        setContent{
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Black),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                    CircularProgressIndicator(modifier = Modifier.background(color = Color.Black), color=Color.Cyan)
            }
        }
    }

    private fun refreshData(){
        val characterName = characterInspectViewModel.getCharacterName()
        if (characterName != null) {
            showLoadingContent()
            inventoryCardsViewModel.getInventoryCardsForCharacter(characterName)
            spellOrAbilityCardsViewModel.getSpellOrAbilityCardsForCharacter(characterName)
            characterInspectViewModel.refreshCharacterData(characterName, true)
        }
    }

    private fun setPanelContent(characterData: Character){
        setContent {
            PathfinderCompanionTheme {
                var tabPage by remember{
                    mutableStateOf(TabPage.StatsAndSkillsPanel)
                }

                Column(modifier = Modifier.background(color = Color.Black)){
                    HeaderPanel(characterData.generalData.longName, characterData.generalData.race, characterData.generalData.characterClass, characterData.image)
                    Scaffold(topBar = { pagerBarComposable.PagerBar(
                        selectedTabIndex = tabPage.ordinal,
                        onSelectedTab = { tabPage = it})},
                        floatingActionButton = { RefreshDataButton()}){
                        when(tabPage.ordinal) {
                            0 -> StatsAndSkillsPanel(characterData)
                            1 -> InventoryPanel()
                            2 -> SpellsAndAbilitiesPanel()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun HeaderPanel(characterName: String, characterRace: String, characterClass: String, image: String) {
        // Header Panel
        Row(modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(all = 8.dp)
            .background(Color.Black)) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = stringResource(R.string.character_portrait_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)//optional
                    .size(100.dp)
                    .fillMaxSize()

            )
            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = characterName,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$characterRace $characterClass",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(all = 4.dp)
                )
            }
        }

    }

    @Composable
    fun RefreshDataButton(){
        FloatingActionButton(
            onClick = { refreshData() },
            backgroundColor = Color.Cyan,
            contentColor = Color.Black,
        ){
            Icon(Icons.Filled.Refresh,"")
        }
    }

    @Composable
    fun InventoryPanel() {
        val cards = inventoryCardsViewModel.cards.collectAsState()
        val expandedCardIds = inventoryCardsViewModel.expandedCardIdsList.collectAsState()
        val inventoryCardComposable = InventoryCardComposable()

        Column{
            // Inventory
            LazyColumn {
                itemsIndexed(cards.value) { _, card ->
                    inventoryCardComposable.InventoryCardComposable(
                        card = card,
                        onCardArrowClick = { inventoryCardsViewModel.onCardArrowClicked(card.id) },
                        expanded = expandedCardIds.value.contains(card.id))
                }
            }
        }
    }

    @Composable
    fun SpellsAndAbilitiesPanel() {
        val cards = spellOrAbilityCardsViewModel.cards.collectAsState()
        val expandedCardIds = spellOrAbilityCardsViewModel.expandedCardIdsList.collectAsState()
        val spellOrAbilityCardComposable = SpellOrAbilityCardComposable()

        Column{
            // Spells and Abilities
            LazyColumn {
                itemsIndexed(cards.value) { _, card ->
                    spellOrAbilityCardComposable.SpellOrAbilityCardComposable(
                        card = card,
                        onCardArrowClick = { spellOrAbilityCardsViewModel.onCardArrowClicked(card.id) },
                        expanded = expandedCardIds.value.contains(card.id))
                }
            }
        }
    }

    @Composable
    fun StatsAndSkillsPanel(characterData: Character) {

        // expansion state toggles
        var defensiveStatsExpansionState by remember { mutableStateOf(false) }
        val defensiveStatsArrowRotationState by animateFloatAsState(targetValue = if (defensiveStatsExpansionState) 0f else 180f)

        var statModifiersExpansionState by remember { mutableStateOf(false) }
        val statModifiersArrowRotationState by animateFloatAsState(targetValue = if (statModifiersExpansionState) 0f else 180f)

        var skillsAndPerceptionExpansionState by remember { mutableStateOf(false) }
        val skillsAndPerceptionArrowRotationState by animateFloatAsState(targetValue = if (skillsAndPerceptionExpansionState) 0f else 180f)

        var biographyExpansionState by remember { mutableStateOf(false) }
        val biographyArrowRotationState by animateFloatAsState(targetValue = if (skillsAndPerceptionExpansionState) 0f else 180f)

        Column {
            // Biography Panel Header
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    biographyExpansionState = !biographyExpansionState
                }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.biography_header_string),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(R.string.dropup_arrow_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.rotate(biographyArrowRotationState)
                )
            }

            // Biography Panel
            AnimatedVisibility(visible = biographyExpansionState) {
                Row(modifier = Modifier.animateContentSize(
                    animationSpec = tween(
                        delayMillis = 300,
                        easing = LinearOutSlowInEasing))) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                        Row{
                            Text(
                                text = stringResource(R.string.character_race_header) + stringResource(R.string.COLON),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(
                                text = characterData.generalData.race,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.character_class_header) + stringResource(R.string.COLON),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(
                                text = characterData.generalData.characterClass,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.character_alignment_header) + stringResource(R.string.COLON),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(
                                text = characterData.generalData.alignment,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.character_deity_header) + stringResource(R.string.COLON),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(
                                text = characterData.generalData.deity,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.character_age_header) + stringResource(R.string.COLON),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(
                                text = characterData.generalData.age,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Row{
                            Text(
                                text = characterData.generalData.notes,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                    }
                }
            }

            // Defensive Stats Panel Header
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    defensiveStatsExpansionState = !defensiveStatsExpansionState
                }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.defensive_statistics_header_string),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(R.string.dropup_arrow_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.rotate(defensiveStatsArrowRotationState)
                )

            }

            // Defensive Stats Panel
            AnimatedVisibility(visible = defensiveStatsExpansionState) {
                Row(
                    modifier = Modifier.animateContentSize(
                        animationSpec = tween(
                            delayMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        // Health AC and Speed
                        Row {
                            Text(
                                text = stringResource(R.string.health_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.weight(.55f)
                            )

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.weight(.45f)
                            )
                            {

                                Text(
                                    text = characterData.defensiveStats.currentHealth.toString(),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.primary
                                )

                                Text(
                                    text = stringResource(R.string.SLASH),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.primary
                                )

                                Text(
                                    text = characterData.defensiveStats.maxHealth.toString(),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                        Row {
                            Text(
                                text = stringResource(R.string.fortitude_saves) + stringResource(R.string.COLON),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.55f)
                            )

                            Text(
                                text = stringResource(R.string.PLUS) + characterData.defensiveStats.fortitudeSave.toString(),
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.45f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        Row{
                            Text(
                                stringResource(R.string.armor_class_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.weight(.55f)
                            )

                            Text(
                                text = characterData.defensiveStats.armorClass.toString(),
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(.45f)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.reflex_saves) + stringResource(R.string.COLON),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.55f)
                            )

                            Text(
                                text = stringResource(R.string.PLUS) + characterData.defensiveStats.reflexSave.toString(),
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.45f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        Row{
                            Text(
                                stringResource(R.string.character_speed_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.weight(.55f)
                            )

                            Text(
                                text = characterData.defensiveStats.characterSpeed.toString() + stringResource(
                                    R.string.feet_abbreviation
                                ),
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(.45f)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.will_saves) + stringResource(R.string.COLON),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.55f)
                            )

                            Text(
                                text = stringResource(R.string.PLUS) + characterData.defensiveStats.willSave.toString(),
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.45f)
                            )
                        }
                    }
                }
            }

            // Stat Modifiers Panel Header
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    statModifiersExpansionState = !statModifiersExpansionState
                }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.stat_modifiers_header_string),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(R.string.dropup_arrow_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.rotate(statModifiersArrowRotationState)
                )

            }


            // Stat Modifiers Panel
            AnimatedVisibility(visible = statModifiersExpansionState) {
                Row(
                    modifier = Modifier.animateContentSize(
                        animationSpec = tween(
                            delayMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        Row {
                            Text(
                                text = stringResource(R.string.strength_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.weight(.55f)
                            )
                            Text(
                                text = when (characterData.abilityModifiers.strengthModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.strengthModifier.toString()
                                    false -> characterData.abilityModifiers.strengthModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(.45f)
                            )
                        }
                        Row {
                            Text(
                                text = stringResource(R.string.int_modifier_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.55f)
                            )
                            Text(
                                text = when (characterData.abilityModifiers.intModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.intModifier.toString()
                                    false -> characterData.abilityModifiers.intModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.45f)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        Row{
                            Text(
                                text = stringResource(R.string.dexterity_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(.55f)
                            )
                            Text(
                                text = when (characterData.abilityModifiers.dexterityModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.dexterityModifier.toString()
                                    false -> characterData.abilityModifiers.dexterityModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(.45f)
                            )
                        }
                        Row{
                            Text(
                                stringResource(R.string.wisdom_modifier_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier
                                    .weight(.55f)
                                    .padding(top = 8.dp)
                            )

                            Text(
                                text = when (characterData.abilityModifiers.wisdomModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.wisdomModifier.toString()
                                    false -> characterData.abilityModifiers.wisdomModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(.45f)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .padding(top = 8.dp)
                            .weight(1f)
                    ) {
                        Row{
                            Text(
                                stringResource(R.string.constitution_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.weight(.55f)
                            )

                            Text(
                                text = when (characterData.abilityModifiers.constitutionModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.constitutionModifier.toString()
                                    false -> characterData.abilityModifiers.constitutionModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(.45f)
                            )
                        }
                        Row{
                            Text(
                                text = stringResource(R.string.charisma_modifier_header_string) + stringResource(
                                    R.string.COLON
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.55f)
                            )

                            Text(
                                text = when (characterData.abilityModifiers.charismaModifier > 0) {
                                    true -> stringResource(R.string.PLUS) + characterData.abilityModifiers.charismaModifier.toString()
                                    false -> characterData.abilityModifiers.charismaModifier.toString()
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .weight(.45f)
                            )
                        }
                    }
                }
            }

            // Skills and Perception Panel Header
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    skillsAndPerceptionExpansionState = !skillsAndPerceptionExpansionState
                }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.skills_and_perception_header_string),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(R.string.dropup_arrow_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.rotate(skillsAndPerceptionArrowRotationState)
                )
            }

            // Skills and Perception Panel
            AnimatedVisibility(visible = skillsAndPerceptionExpansionState){
                LazyColumn {
                    items(characterData.skills.skillList.size) { index ->
                        Text(
                            text = characterData.skills.skillList[index],
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .padding(end = 8.dp)
                                .border(1.dp, Color.Gray)
                                .padding(all = 8.dp)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode")
    @Composable
    fun PreviewCharacterDataPanel() {
        PathfinderCompanionTheme{

        }
    }
}
