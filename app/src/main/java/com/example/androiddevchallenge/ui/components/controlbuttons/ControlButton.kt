package com.example.androiddevchallenge.ui.components.controlbuttons

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.utils.animation.colorOnPressedAnimation
import com.example.androiddevchallenge.ui.utils.animation.floatOnBooleanAnimation
import com.example.androiddevchallenge.ui.utils.modifiers.translate
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ControlButton(
    onClick: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    colorNormal: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
    colorPressed: Color = MaterialTheme.colors.onBackground,
    animationDuration: Int = defaultAnimationDuration,
    translateYAnimationValue: Float = defaultTranslateYAnimationValue,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (color: Color) -> Unit,
) {
    val color = colorOnPressedAnimation(colorNormal, colorPressed, interactionSource)

    val alphaTargetValue = if (isVisible) 1f else 0f
    val alpha = rememberAnimatable(alphaTargetValue)
    LaunchedEffect(isVisible) {
        alpha.animateTo(alphaTargetValue, tween(animationDuration))
    }

    val translateYTargetValue = if (isVisible) 0f else translateYAnimationValue
    val translateY = rememberAnimatable(translateYTargetValue)
    LaunchedEffect(isVisible) {
        translateY.animateTo(translateYTargetValue, tween(animationDuration))
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(65.dp)
            .width(65.dp)
            .translate(y = translateY.value)
    ) {
        OutlinedButton(
            onClick = onClick,
            enabled = isVisible,
            border = BorderStroke(2.dp, color.value),
            shape = CircleShape,
            colors = object : ButtonColors {
                @Composable
                override fun backgroundColor(enabled: Boolean): State<Color> {
                    return mutableStateOf(MaterialTheme.colors.background.copy(alpha = 0f))
                }

                @Composable
                override fun contentColor(enabled: Boolean): State<Color> {
                    return mutableStateOf(color.value)
                }
            },
            contentPadding = PaddingValues(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 8.dp,
            ),
            interactionSource = interactionSource,
            modifier = modifier
                .size(65.dp)
                .alpha(alpha.value),
        ) {
            content(color.value)
        }
    }
}

private const val defaultAnimationDuration = 800
private const val defaultTranslateYAnimationValue = 24f
