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
import com.jonesdev.pathfindercompanion.data.model.InventoryCard
import com.jonesdev.pathfindercompanion.data.model.InventoryItem

class InventoryCardComposable {

    @SuppressLint("UnusedTransitionTargetStateParameter")
    @Composable
    fun InventoryCardComposable(card: InventoryCard, onCardArrowClick: () -> Unit, expanded: Boolean) {
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
                    InventoryCardArrow(
                        degrees = arrowRotationDegree,
                        onClick = onCardArrowClick
                    )
                    InventoryCardTitle(title = card.title)
                }
                InventoryCardExpandedContent(visible = expanded, initialVisibility = expanded, card.inventoryItem)
            }
        }
    }

    @Composable
    fun InventoryCardTitle(title: String) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }

    @Composable
    fun InventoryCardArrow(degrees: Float, onClick: () -> Unit) {
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
    fun InventoryCardExpandedContent(visible: Boolean = true, initialVisibility: Boolean = false, inventoryItem: InventoryItem) {
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
                // count
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.item_count_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = inventoryItem.count.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(1.dp))

                // bulk
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.item_bulk_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = inventoryItem.bulk.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(8.dp))

                // price
                Row(Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(R.string.item_price_header) + stringResource(R.string.COLON),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.widthIn(1.dp))
                    Text(
                        text = inventoryItem.price,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.heightIn(8.dp))

                // description
                Text(
                    text = inventoryItem.description,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp).padding(start = 8.dp).padding(end = 8.dp)
                )
            }
        }
    }

}