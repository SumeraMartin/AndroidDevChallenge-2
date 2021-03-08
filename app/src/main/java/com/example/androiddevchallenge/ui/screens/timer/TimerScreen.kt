package com.example.androiddevchallenge.ui.screens.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.components.countdown.CountdownDigits
import com.example.androiddevchallenge.ui.components.controlbuttons.ControlPanel
import com.example.androiddevchallenge.ui.components.irregularcircles.DynamicIrregularCircles
import com.example.androiddevchallenge.ui.components.keyboard.DigitsKeyboard
import com.example.androiddevchallenge.ui.data.Digit
import com.example.androiddevchallenge.ui.theme.TimerTheme
import com.example.androiddevchallenge.ui.theme.blueAzure
import com.example.androiddevchallenge.ui.theme.blueHour
import com.example.androiddevchallenge.ui.theme.blueSunny
import com.example.androiddevchallenge.ui.theme.greyBlue
import com.example.androiddevchallenge.ui.theme.greyTrick
import com.example.androiddevchallenge.ui.theme.purpleOutside
import com.example.androiddevchallenge.ui.utils.back.BackPressHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun TimerScreen() {
    val state = remember { mutableStateOf(TimerScreenState.DEFAULT) }

    val onStartClicked = {
        if (!state.value.digits.isFinished) {
            state.value = state.value.nextStateAfterOnStartClicked(
                tickDuration = tickDuration
            )
        }
    }

    val onStopClicked = {
        state.value = state.value.nextStateAfterOnStopClicked(
            returnEffectDuration = tickDuration,
        )
    }

    val onPauseClicked = {
        state.value = state.value.nextStateAfterOnPauseClicked(
            returnEffectDuration = tickDuration,
        )
    }

    val onDigitClicked = { digit: Digit ->
        if (state.value.digits.isPossibleToAddNextDigit()) {
            state.value = state.value.nextStateAfterDigitClicked(
                digit = digit,
                effectDuration = quickLightDuration,
                returnBackDuration = quickLightReturnBackDuration,
            )
        }
    }

    val onRemoveLastClicked = {
        state.value = state.value.nextStateAfterRemoveLastClicked(
            effectDuration = quickLightDuration,
            returnBackDuration = quickLightReturnBackDuration,
        )
    }

    val onTick: () -> TimerScreenState = {
        val newState = state.value.nextStateAfterTick(
            tickDuration = tickDuration,
        )
        state.value = newState
        newState
    }

    val onFinished = {
        state.value = state.value.nextStateAfterFinished(
            returnEffectDuration = tickDuration,
        )
    }

    LaunchedEffect(state.value.isTimerRunning) {
        while (isActive && state.value.isTimerRunning) {
            delay(tickDuration.toLong())

            val newState = onTick()
            if (newState.digits.isFinished) {
                onFinished()
                break
            }
        }
    }

    BackPressHandler(isActive = state.value.isTimerRunning) {
        onStopClicked()
    }

    Column(Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(320.dp).padding(20.dp),
        ) {
            DynamicIrregularCircles(
                circleColors = listOf(blueSunny, blueHour, greyTrick, purpleOutside, blueAzure),
                lightTopCircleColor = greyBlue,
                state = state.value.irregularCirclesState,
                modifier = Modifier
            )
            CountdownDigits(
                digits = state.value.digits,
                showNextDigitsIfNeeded = state.value.isTimerRunning,
                showDescription = !state.value.isTimerRunning,
                nextDigits = state.value.digits.nextDigits(),
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            DigitsKeyboard(
                isVisible = state.value.isKeyboardVisible,
                onDigitClicked = onDigitClicked,
                onRemoveLastClicked = onRemoveLastClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .align(Alignment.TopCenter)
            )
            ControlPanel(
                onStartClicked = onStartClicked,
                onPauseClicked = onPauseClicked,
                onStopClicked = onStopClicked,
                controlButtonState = state.value.controlButtonState,
                normalHeight = 120.dp,
                expandedHeight = 400.dp,
                isExpanded = state.value.isConrolPanelExpanded,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }



//        val isControlPanelExpanded =
//        val translateYTargetValue = if (isVisible) 0f else translateYAnimationValue
//        val translateY = rememberAnimatable(translateYTargetValue)
//        LaunchedEffect(isVisible) {
//            translateY.animateTo(translateYTargetValue, tween(animationDuration))
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth().height(100.dp)
//        ) {
//            PlayControlButton(
//                onClick = onStartClicked,
//                isVisible = state.value.controlButtonState.isPlayButtonVisible,
//            )
//            PauseControlButton(
//                onClick = onPauseClicked,
//                isVisible = state.value.controlButtonState.isPauseButtonVisible,
//            )
//            StopControlButton(
//                onClick = onStopClicked,
//                isVisible = state.value.controlButtonState.isStopButtonVisible,
//            )
//        }
    }
}

private const val quickLightDuration = 50
private const val quickLightReturnBackDuration = 600
private const val tickDuration = 1000

@Preview
@Composable
fun TimerScreenPreview() {
    TimerTheme {
        TimerScreen()
    }
}