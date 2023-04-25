package com.example.scorekeeper.game.renderers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.game.types.SingleWinRoundGame
import com.example.scorekeeper.ui.theme.Purple200
import com.example.scorekeeper.ui.theme.Purple500

open class SingleWinRoundGameRenderer<T>(game: T,
                                         appViewModel: AppViewModel
) : GameRenderer<SingleWinRoundGame>(game, appViewModel) where T : SingleWinRoundGame {
    fun LazyListScope.previousRoundDisplay() {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
                    .clickable { game.roundDisplayCollapsed = !game.roundDisplayCollapsed }
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
        if(!game.roundDisplayCollapsed) {
            itemsIndexed(game.rounds) { index, round ->
                round.GetRoundCard(roundIndex = index + 1)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(nameSortingModalState: ModalBottomSheetState) {
        item {
            Text(text = "Current Round", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(nameSortingModalState)

        item {
            Text(text = "Previous Rounds", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        previousRoundDisplay()
    }

    @Composable
    override fun ScoreUpdateInputs(cardName: String) {
        Button(
            onClick = { game.scoreUpdateInteract(cardName) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple200),
            border = BorderStroke(0.dp, MaterialTheme.colors.background),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Winner", fontWeight = FontWeight.Black, fontSize = 20.sp, color = MaterialTheme.colors.onSurface)
        }
    }
}