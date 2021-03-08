/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
