package com.example.androiddevchallenge.ui.components.keyboard.keys

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.data.Digit

@Composable
fun DigitKeyboardKey(
    digit: Digit,
    isVisible: Boolean,
    onDigitClicked: (Digit) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = defaultFontSize,
) {
    KeyboardKey(
        isVisible = isVisible,
        onClick = { onDigitClicked(digit) },
        modifier = modifier,
    ) { color ->
        Text(digit.stringValue, color = color, fontSize = fontSize)
    }
}

private val defaultRippleRadius = 40.dp
private val defaultFontSize = 24.sp
