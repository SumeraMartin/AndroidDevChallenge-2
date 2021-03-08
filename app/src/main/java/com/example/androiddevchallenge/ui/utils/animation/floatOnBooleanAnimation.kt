package com.example.androiddevchallenge.ui.utils.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun floatOnBooleanAnimation(
    initialValue: Float,
    targetValue: Float,
    showTarget: Boolean,
): Animatable<Float, AnimationVector1D> {
    val animatable = remember { Animatable(initialValue) }
    val value = if (showTarget) targetValue else initialValue
    LaunchedEffect(value, tween<Float>()) {
        animatable.animateTo(value)
    }
    return animatable
}
