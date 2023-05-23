package com.example.scorekeeper.game.renderers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.scorekeeper.game.types.RoundGame
import com.example.scorekeeper.ui.theme.Purple500

abstract class RoundGameRenderer(override val game: RoundGame) : GameRenderer() {
    private var roundDisplayCollapsed = mutableStateOf(false)

    internal fun LazyListScope.previousRoundDisplay() {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
                    .clickable {
                        roundDisplayCollapsed.value = !roundDisplayCollapsed.value
                    }
            ) {
                HeaderText(name = "Round #")
                HeaderText(name = "Placements")
            }
        }

        // TODO proper collapsed visuals
        if (!roundDisplayCollapsed.value) {
            itemsIndexed(game.rounds) { index, round ->
                round.getRenderer().RoundCard(roundIndex = index + 1)
            }
        }
    }
}