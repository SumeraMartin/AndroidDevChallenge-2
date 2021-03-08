package com.example.androiddevchallenge.ui.components.keyboard

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.components.keyboard.keys.DigitKeyboardKey
import com.example.androiddevchallenge.ui.components.keyboard.keys.RemoveLastKeyboardKey
import com.example.androiddevchallenge.ui.data.Digit
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable

@Composable
fun DigitsKeyboard(
    isVisible: Boolean,
    onDigitClicked: (Digit) -> Unit,
    onRemoveLastClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isVisibleDuration: Int = defaultIsVisibleDuration,
    isVisibleAnimationTranslateY: Float = defaultIsVisibleTranslateYAnimation,
) {

    val translateYTargetValue = if (isVisible) 0f else isVisibleAnimationTranslateY
    val translateY = rememberAnimatable(translateYTargetValue)
    LaunchedEffect(isVisible) {
        translateY.animateTo(translateYTargetValue, tween(isVisibleDuration))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(
            start = 0.dp,
            end = 0.dp,
            bottom = 0.dp,
            top = translateY.value.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                DigitKeyboardKey(
                    digit = Digit.ONE,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.TWO,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.THREE,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
            )  {
                DigitKeyboardKey(
                    digit = Digit.FOUR,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.FIVE,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.SIX,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
            )  {
                DigitKeyboardKey(
                    digit = Digit.SEVEN,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.EIGHT,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.NINE,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
            )  {
                RemoveLastKeyboardKey(
                    isVisible = isVisible,
                    onClick = onRemoveLastClicked,
                )
                DigitKeyboardKey(
                    digit = Digit.ZERO,
                    onDigitClicked = onDigitClicked,
                    isVisible = isVisible,
                )
                DigitKeyboardKey(
                    digit = Digit.ZERO,
                    onDigitClicked = onDigitClicked,
                    isVisible = false,
                )

            }
        }
    }
}

private const val defaultIsVisibleDuration = 800
private const val defaultIsVisibleTranslateYAnimation = 24f

@Preview
@Composable
fun NumericKeyboardPreview() {
    DigitsKeyboard(
        isVisible = true,
        onDigitClicked = {},
        onRemoveLastClicked = {}
    )
}