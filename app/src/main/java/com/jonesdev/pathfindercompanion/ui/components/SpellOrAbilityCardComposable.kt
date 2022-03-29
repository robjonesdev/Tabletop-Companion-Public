package com.jonesdev.pathfindercompanion.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jonesdev.pathfindercompanion.R
import com.jonesdev.pathfindercompanion.data.model.SpellOrAbilityCard
import com.jonesdev.pathfindercompanion.data.model.Ability

class SpellOrAbilityCardComposable {

    @SuppressLint("UnusedTransitionTargetStateParameter")
    @Composable
    fun SpellOrAbilityCardComposable(card: SpellOrAbilityCard, onCardArrowClick: () -> Unit, expanded: Boolean) {
        val expandAnimationDuration = 600
        val transitionState = remember {
            MutableTransitionState(expanded).apply {
                targetState = !expanded
            }
        }
        val transition = updateTransition(transitionState, label = "")
        val cardBgColor by transition.animateColor({
            tween(durationMillis = expandAnimationDuration) }, label = "") { if (expanded) Color.Gray else Color.Gray}
        val cardPaddingHorizontal by transition.animateDp({
            tween(durationMillis = expandAnimationDuration) }, label = "") {
            if (expanded) 48.dp else 24.dp }
        val cardElevation by transition.animateDp({
            tween(durationMillis = expandAnimationDuration) }, label = "") {
            if (expanded) 24.dp else 4.dp }
        val cardRoundedCorners by transition.animateDp({
            tween(
                durationMillis = expandAnimationDuration,
                easing = FastOutSlowInEasing
            )
        }, label = "") {
            if (expanded) 0.dp else 16.dp }
        val arrowRotationDegree by transition.animateFloat({
            tween(durationMillis = expandAnimationDuration) }, label = "") {
            if (expanded) 0f else 180f }

        Card(
            backgroundColor = cardBgColor,
            contentColor = Color.White,
            elevation = cardElevation,
            shape = RoundedCornerShape(cardRoundedCorners),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = cardPaddingHorizontal,
                    vertical = 8.dp
                )) {
            Column {
                Box {
                    SpellOrAbilityCardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick
                    )
                    SpellOrAbilityCardTitle(title = card.title)
                }
                SpellOrAbilityCardExpandedContent(visible = expanded, initialVisibility = expanded, card.spellOrAbility)
            }
        }
    }

    @Composable
    fun SpellOrAbilityCardTitle(title: String) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }

    @Composable
    fun SpellOrAbilityCardArrow(degrees: Float, onClick: () -> Unit) {
        IconButton(
            onClick = onClick,
            content = {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier.rotate(degrees),
                )
            }
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun SpellOrAbilityCardExpandedContent(visible: Boolean = true, initialVisibility: Boolean = false, spellOrAbility: Ability) {
        val fadeInAnimationDuration = 600
        val expandAnimationDuration = 600
        val fadeOutAnimationDuration = 600
        val collapseAnimationDuration = 600
        val enterFadeIn = remember {
            fadeIn(
                animationSpec = TweenSpec(
                    durationMillis = fadeInAnimationDuration,
                    easing = FastOutLinearInEasing
                )
            )
        }
        val enterExpand = remember {
            expandVertically(animationSpec = tween(expandAnimationDuration))
        }
        val exitFadeOut = remember {
            fadeOut(
                animationSpec = TweenSpec(
                    durationMillis = fadeOutAnimationDuration,
                    easing = LinearOutSlowInEasing
                )
            )
        }
        val exitCollapse = remember {
            shrinkVertically(animationSpec = tween(collapseAnimationDuration))
        }
        AnimatedVisibility(
            visible = visible,
            initiallyVisible = initialVisibility,
            enter = enterExpand + enterFadeIn,
            exit = exitCollapse + exitFadeOut,
        ) {
            Column(modifier = Modifier.heightIn(25.dp)) {
                // cost
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.spell_or_ability_action_cost_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = spellOrAbility.cost,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(1.dp))

                // duration
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.spell_or_ability_duration_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = spellOrAbility.duration,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(8.dp))

                // range
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.spell_or_ability_range_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = spellOrAbility.range,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(8.dp))

                // level
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.spell_or_ability_level_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = spellOrAbility.level,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(8.dp))

                // description
                Text(
                    text = spellOrAbility.description,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp).padding(start = 8.dp).padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.heightIn(6.dp))

                // heightening info
                Text(
                    text = spellOrAbility.heighteningInfo,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp).padding(start = 8.dp).padding(end = 8.dp)
                )
            }
        }
    }

}