package com.example.scorekeeper.game.renderers

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.game.Player
import com.example.scorekeeper.game.round.Round
import com.example.scorekeeper.game.round.RoundRenderer
import com.example.scorekeeper.game.types.SingleWinRoundGame
import com.example.scorekeeper.ui.theme.Purple200
import com.example.scorekeeper.ui.theme.Purple500
import kotlinx.coroutines.launch

class SingleWinRoundGameRenderer(override val game: SingleWinRoundGame) : RoundGameRenderer(game) {
    private suspend fun scoreUpdateInteract(
        appViewModel: AppViewModel,
        context: Context,
        player: Player
    ) {
        updateScore(appViewModel, context, player, 1)
        game.rounds.add(Round(mapOf(player to 1)))
        appViewModel.setActiveGame(context, game)
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        item {
            Text(
                text = "Current Round",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(appViewModel, nameSortingModalState)

        item {
            Text(
                text = "Previous Rounds",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        previousRoundDisplay(appViewModel)
    }

    @Composable
    override fun ScoreUpdateInputs(appViewModel: AppViewModel, player: Player) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Button(
            onClick = { scope.launch { scoreUpdateInteract(appViewModel, context, player) } },
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple200),
            border = BorderStroke(0.dp, MaterialTheme.colors.background),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Winner",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}