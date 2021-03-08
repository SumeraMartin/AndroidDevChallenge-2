package com.example.androiddevchallenge.ui.components.controlbuttons

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
    ){

        // Play button visible in collapsed state
        Box(modifier = modifier
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