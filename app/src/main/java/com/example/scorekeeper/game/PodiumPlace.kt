package com.example.scorekeeper.game

import com.example.scorekeeper.R

enum class PodiumPlace(val rankingInt: Int, val colorId: Int) {
    FIRST(1, R.color.gold),
    SECOND(2, R.color.silver),
    THIRD(3, R.color.bronze)
}