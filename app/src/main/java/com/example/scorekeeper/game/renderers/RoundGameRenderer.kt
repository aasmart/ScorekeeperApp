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
import androidx.compose.material.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.game.types.RoundGame
import com.example.scorekeeper.ui.theme.Purple500
import kotlinx.coroutines.launch

open class RoundGameRenderer(override val game: RoundGame) : GameRenderer() {
    private var roundDisplayCollapsed = false

    internal fun LazyListScope.previousRoundDisplay(appViewModel: AppViewModel) {
        item {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
                    .clickable {
                        roundDisplayCollapsed = !roundDisplayCollapsed
                        scope.launch {
                            appViewModel.setActiveGame(
                                context,
                                game
                            )
                        }
                    }
            ) {
                Text(
                    text = "Round #",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Text(
                    text = "Placements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
            }
        }

        // TODO proper collapsed visuals
        if (!roundDisplayCollapsed) {
            itemsIndexed(game.rounds) { index, round ->
                round.getRenderer().RoundCard(roundIndex = index + 1)
            }
        }
    }
}