package com.example.androiddevchallenge.ui.utils.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent

/**
 * A [Modifier] which translate y position
 */
fun Modifier.translate(y: Float = 0f): Modifier = drawWithContent {
    drawContext.transform.translate(0f, y)
    drawContent()
    drawContext.transform.translate(0f, -y)
}
