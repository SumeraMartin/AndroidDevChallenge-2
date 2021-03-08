package com.example.androiddevchallenge.ui.components.irregularcircles

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size

import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable
import com.example.androiddevchallenge.ui.theme.purpleSnake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.isActive
import kotlin.math.min
import kotlin.random.Random

@Composable
fun DynamicIrregularCircle(
    circleColor: Color,
    state: IrregularCirclesState,
    modifier: Modifier = Modifier,
    reactToLightEffect: Boolean = false,
    circleWidth: Float = defaultCircleWidth,
    animationRadiusRange: (size: Int) -> IntRange = { size -> calculateDefaultRadiusRange(size) },
    animationTranslateRange: (size: Int) -> IntRange = { size -> calculateTranslateRanges(size) },
    animationChangeDurationRange: IntRange = defaultAnimationDurationRange,
    scaleAnimationChange: Float = defaultScaleAnimationChange,
) {

    BoxWithConstraints(modifier = modifier) {
        val height = constraints.maxHeight
        val width = constraints.maxWidth
        val size = min(width, height)
        val calculatedAnimationRadiusRange = animationRadiusRange(size)

        // Random generators
        val randomRadiusRangeValue = { getRandomRadiusValue(calculatedAnimationRadiusRange) }
        val randomScaleValue = { getRandomScaleAnimationValue(scaleAnimationChange) }
        val randomAlphaValue = { getRandomAlphaAnimationValue(defaultAlphaAnimationChange) }
        val randomTranslateValue = { getRandomTranslateValue(animationTranslateRange(size)) }
        val randomDurationValue = { getRandomDurationValue(animationChangeDurationRange) }

        // Defaults
        val radiusDefault = calculatedAnimationRadiusRange.last.toFloat()
        val topLeftRadius = rememberAnimatable(radiusDefault)
        val topRightRadius = rememberAnimatable(radiusDefault)
        val bottomLeftRadius = rememberAnimatable(radiusDefault)
        val bottomRightRadius = rememberAnimatable(radiusDefault)

        val scaleDefault = 1f
        val scaleX = rememberAnimatable(scaleDefault)
        val scaleY = rememberAnimatable(scaleDefault)

        val translateDefault = 0f
        val translateX = rememberAnimatable(translateDefault)
        val translateY = rememberAnimatable(translateDefault)

        val alphaDefault = 1f
        val alpha = rememberAnimatable(alphaDefault)

        val animateToVibrateState: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
            awaitAll(
                async { topLeftRadius.animateTo(randomRadiusRangeValue(), tween(duration)) },
                async { topRightRadius.animateTo(randomRadiusRangeValue(), tween(duration)) },
                async { bottomLeftRadius.animateTo(randomRadiusRangeValue(), tween(duration)) },
                async { bottomRightRadius.animateTo(randomRadiusRangeValue(), tween(duration)) },
                async { scaleX.animateTo(randomScaleValue(), tween(duration)) },
                async { scaleY.animateTo(randomScaleValue(), tween(duration)) },
                async { translateX.animateTo(randomTranslateValue(), tween(duration)) },
                async { translateY.animateTo(randomTranslateValue(), tween(duration)) },
                async { alpha.animateTo(randomAlphaValue(), tween(duration)) },
            )
        }

        val animateToDefaultState: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
            awaitAll(
                async { topLeftRadius.animateTo(radiusDefault, tween(duration)) },
                async { topRightRadius.animateTo(radiusDefault, tween(duration)) },
                async { bottomLeftRadius.animateTo(radiusDefault, tween(duration)) },
                async { bottomRightRadius.animateTo(radiusDefault, tween(duration)) },
                async { scaleX.animateTo(scaleDefault, tween(duration)) },
                async { scaleY.animateTo(scaleDefault, tween(duration)) },
                async { translateX.animateTo(translateDefault, tween(duration)) },
                async { translateY.animateTo(translateDefault, tween(duration)) },
                async { alpha.animateTo(alphaDefault, tween(duration)) },
            )
        }

        val animateLightState: suspend CoroutineScope.(duration: Int, backDuration: Int) -> Unit = { duration, backDuration ->
            alpha.animateTo(1f, tween(duration))
            alpha.animateTo(0f, tween(backDuration))
        }

        LaunchedEffect(state.eventId) {
            when (state.eventEffect) {
                IrregularCirclesEffect.Idle -> {

                }
                is IrregularCirclesEffect.Vibrate -> {
                    val duration = state.eventEffect.duration
                    animateToVibrateState(duration)
                }
                is IrregularCirclesEffect.VibrateOnceAndReturnBack -> {
                    val durationToVibrate = state.eventEffect.durationToVibrate
                    animateToVibrateState(durationToVibrate)

                    val durationToBack = state.eventEffect.durationToBack
                    animateToDefaultState(durationToBack)
                }
                is IrregularCirclesEffect.ReturnBack -> {
                    val durationToBack = state.eventEffect.durationToBack
                    animateToDefaultState(durationToBack)
                }
                is IrregularCirclesEffect.Light -> {
                    if (reactToLightEffect) {
                        animateLightState(state.eventEffect.duration, state.eventEffect.durationToBack)
                    }
                }
            }
        }

        val stroke = remember { Stroke(width = circleWidth) }

        Canvas(modifier = Modifier.size(size.dp)) {
            val path = Path().apply {
                val rect = Rect(center, size / 2f)
                val radius1 = CornerRadius(topLeftRadius.value)
                val radius2 = CornerRadius(topRightRadius.value)
                val radius3 = CornerRadius(bottomLeftRadius.value)
                val radius4 = CornerRadius(bottomRightRadius.value)
                addRoundRect(RoundRect(rect, radius1, radius2, radius3, radius4))
            }

            val circleColorWithAnimation = circleColor.copy(alpha = alpha.value)
            translate(translateX.value, translateY.value) {
                scale(scaleX.value, scaleY.value) {
                    drawPath(path, circleColorWithAnimation, style = stroke)
                }
            }

        }
    }
}

private fun calculateDefaultRadiusRange(size: Int): IntRange {
    val halfSize = size / 2f
    val changeSize = size / 8f
    return (halfSize).toInt() .. (halfSize + changeSize).toInt()
}

private fun calculateTranslateRanges(size: Int): IntRange {
    val change = size / 25f
    return (-change).toInt() .. (change).toInt()
}

private fun getRandomScaleAnimationValue(scaleAnimationChange: Float): Float {
    val from = (1f - scaleAnimationChange).toDouble()
    val to = (1f + scaleAnimationChange).toDouble()
    return Random.nextDouble(from, to).toFloat()
}

private fun getRandomAlphaAnimationValue(alphaAnimationChange: Float): Float {
    val from = (1f - alphaAnimationChange).toDouble()
    return Random.nextDouble(from, 1.0).toFloat()
}

private fun getRandomRadiusValue(radiusRange: IntRange) = radiusRange.random().toFloat()

private fun getRandomDurationValue(durationRange: IntRange) = durationRange.random()

private fun getRandomTranslateValue(translateRange: IntRange) = translateRange.random().toFloat()

private const val defaultCircleWidth = 10f
private const val defaultScaleAnimationChange = 0.1f
private const val defaultAlphaAnimationChange = 0.3f
private val defaultAnimationDurationRange = 1000..1000
private val defaultIsVisibleScaleAnimationDurationRange = 800..1500
private val defaultIsVisibleAlphaAnimationDurationRange = 400..1000

@Preview(widthDp = 360, heightDp = 360)
@Composable
fun DynamicIrregularCirclePreview() {
    DynamicIrregularCircle(
        circleColor = purpleSnake,
        state = IrregularCirclesState(0, IrregularCirclesEffect.Idle),
    )
}