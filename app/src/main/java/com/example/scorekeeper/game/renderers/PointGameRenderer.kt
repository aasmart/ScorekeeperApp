package com.example.scorekeeper.game.renderers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.game.players.AbstractPlayer
import com.example.scorekeeper.game.types.PointGame
import kotlinx.coroutines.launch

class PointGameRenderer(override val game: PointGame) : AbstractGameRenderer() {
    @Composable
    override fun UpdateScoreInputs(appViewModel: AppViewModel, player: AbstractPlayer) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .border(0.75.dp, MaterialTheme.colors.background, RoundedCornerShape(10))
        ) {
            Button(
                onClick = { scope.launch { updateScore(appViewModel, context, player, -1) } },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                border = BorderStroke(0.dp, MaterialTheme.colors.background),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, true)
            ) {
                Text(
                    text = "-",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Button(
                onClick = { scope.launch { updateScore(appViewModel, context, player) } },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                border = BorderStroke(0.dp, MaterialTheme.colors.background),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, true)
            ) {
                Text(
                    text = "+",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        item {
            Text(
                text = "Scoring",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoringList(appViewModel, nameSortingModalState)
    }
}