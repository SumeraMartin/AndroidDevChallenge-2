package com.example.androiddevchallenge.ui.utils.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun colorOnPressedAnimation(
    initialValue: Color,
    pressedValue: Color,
    interactionSource: MutableInteractionSource,
    pressAnimationDuration: Int = defaultPressDuration,
    releaseAnimationDuration: Int = defaultReleaseDuration,
): Animatable<Color, AnimationVector4D> {
    val animatable = remember { androidx.compose.animation.Animatable(initialValue) }
    val isPressed = interactionSource.collectIsPressedAsState()
    LaunchedEffect(isPressed.value) {
        val duration = if (isPressed.value) pressAnimationDuration else releaseAnimationDuration
        val value = if (isPressed.value) pressedValue else initialValue
        animatable.animateTo(value, tween(duration))
    }
    return animatable
}

private const val defaultPressDuration = 50
private const val defaultReleaseDuration = 1500
