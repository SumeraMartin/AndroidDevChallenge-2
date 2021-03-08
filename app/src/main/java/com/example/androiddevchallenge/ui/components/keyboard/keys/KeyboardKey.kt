package com.example.androiddevchallenge.ui.components.keyboard.keys

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.whiteOriginal
import com.example.androiddevchallenge.ui.utils.animation.colorOnPressedAnimation
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable

@Composable
fun KeyboardKey(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = defaultWidth,
    height: Dp = defaultHeight,
    isVisibleDuration: Int = defaultIsVisibleDuration,
    digitColorNormal: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
    digitColorPressed: Color = MaterialTheme.colors.onBackground,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(
        bounded = false,
        radius = defaultRippleRadius,
        color = whiteOriginal
    ),
    content: @Composable (color: Color) -> Unit
) {

    val alphaInitialValue = if (isVisible) 1f else 0f
    val alphaTargetValue = if (isVisible) 1f else 0f
    val alpha = rememberAnimatable(alphaInitialValue)
    LaunchedEffect(isVisible) {
        alpha.animateTo(alphaTargetValue, tween(isVisibleDuration))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width = width, height = height)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = isVisible,
                onClick = onClick
            )
            .alpha(alpha.value)
    ) {
        val color = colorOnPressedAnimation(digitColorNormal, digitColorPressed, interactionSource)
        content(color.value)
    }
}

private val defaultWidth = 110.dp
private val defaultHeight = 90.dp
private val defaultRippleRadius = 40.dp
private const val defaultIsVisibleDuration = 800

@Preview
@Composable
fun KeyboardKeyPreview() {
    KeyboardKey(
        isVisible = true,
        onClick = {},
        content = {},
    )
}
