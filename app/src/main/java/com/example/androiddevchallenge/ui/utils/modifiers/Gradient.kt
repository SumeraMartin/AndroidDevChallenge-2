package com.example.androiddevchallenge.ui.utils.modifiers

import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient

fun Modifier.verticalGradientBackground(colors: List<Color>, alpha: Float = 1f) =
    background(brush = Brush.verticalGradient(colors = colors), alpha = alpha)
