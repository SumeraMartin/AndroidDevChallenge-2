package com.example.androiddevchallenge.ui.utils.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun floatOnPressedAnimation(
    initialValue: Float,
    pressedValue: Float,
    interactionSource: MutableInteractionSource,
): Animatable<Float, AnimationVector1D> {
    val animatable = remember { Animatable(initialValue) }
    val isPressed = interactionSource.collectIsPressedAsState()
    val value = if (isPressed.value) pressedValue else initialValue
    LaunchedEffect(value, tween<Float>()) {
        animatable.animateTo(value)
    }
    return animatable
}