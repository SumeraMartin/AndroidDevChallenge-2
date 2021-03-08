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
package com.example.androiddevchallenge.ui.components.controlbuttons

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.data.ControlButtonState
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable

@Composable
fun ControlPanel(
    onStartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    controlButtonState: ControlButtonState,
    normalHeight: Dp,
    expandedHeight: Dp,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    animationDuration: Int = defaultAnimationDuration,
) {
    val heightTargetValue = if (isExpanded) expandedHeight.value else normalHeight.value
    val height = rememberAnimatable(heightTargetValue)
    LaunchedEffect(isExpanded) {
        height.animateTo(heightTargetValue, tween(animationDuration))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.value.dp)
    ) {

        // Play button visible in collapsed state
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .wrapContentWidth()
                .height(height.value.dp)
        ) {
            PlayControlButton(
                onClick = onStartClicked,
                isVisible = controlButtonState.isPlayButtonVisible && !isExpanded,
            )
        }

        // Buttons visible in expanded state
        Row(
            modifier = modifier
                .wrapContentWidth()
                .height(height.value.dp)
                .align(Alignment.Center)
        ) {
            Box {
                PlayControlButton(
                    onClick = onStartClicked,
                    isVisible = controlButtonState.isPlayButtonVisible && isExpanded,
                )
                PauseControlButton(
                    onClick = onPauseClicked,
                    isVisible = controlButtonState.isPauseButtonVisible,
                )
            }
            StopControlButton(
                onClick = onStopClicked,
                isVisible = controlButtonState.isStopButtonVisible,
            )
        }
    }
}

private const val defaultAnimationDuration = 800
