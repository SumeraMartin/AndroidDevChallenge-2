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
package com.example.androiddevchallenge.ui.components.countdown

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.whiteOriginal
import com.example.androiddevchallenge.ui.utils.modifiers.translate
import com.example.androiddevchallenge.ui.utils.remember.rememberAnimatable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@Composable
fun CountdownDigit(
    currentDigit: String,
    nextDigit: String,
    showNextDigitsIfNeeded: Boolean,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = defaultFontSize,
) {

    val lastDigit = remember { mutableStateOf(currentDigit) }

    val nextDigitAlphaDefault = 0f
    val nextDigitAlpha = rememberAnimatable(nextDigitAlphaDefault)
    val nextDigitTranslateDefault = 0f
    val nextDigitTranslateAddition = rememberAnimatable(nextDigitTranslateDefault)

    val previousDigitAlphaDefault = 0f
    val previousDigitAlpha = rememberAnimatable(previousDigitAlphaDefault)
    val previousDigitTranslateDefault = 0f
    val previousDigitTranslateAddition = rememberAnimatable(previousDigitTranslateDefault)

    val fixedDigitAlpha = remember { mutableStateOf(1f) }
    val fixedDigitText = remember(currentDigit) { mutableStateOf(currentDigit) }

    val moveToDefault: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { nextDigitAlpha.animateTo(nextDigitAlphaDefault, tween(duration)) },
            async { nextDigitTranslateAddition.snapTo(nextDigitTranslateDefault) },
            async { previousDigitAlpha.animateTo(previousDigitAlphaDefault, tween(duration)) },
            async { previousDigitTranslateAddition.snapTo(previousDigitTranslateDefault) },
            async { fixedDigitAlpha.value = 1f },
            async { fixedDigitText.value = currentDigit }
        )
    }

    val showNextDigitHalfVisibility: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { nextDigitAlpha.animateTo(0.3f, tween(duration)) },
        )
    }

    val showNextDigitFullVisibility: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { nextDigitAlpha.animateTo(1f, tween(duration)) },
        )
    }

    val hideNextDigit: suspend CoroutineScope.() -> Unit = {
        awaitAll(
            async { nextDigitAlpha.snapTo(nextDigitAlphaDefault) },
        )
    }

    val showPreviousDigit: suspend CoroutineScope.() -> Unit = {
        awaitAll(
            async { previousDigitAlpha.snapTo(1f) },
        )
    }

    val hidePreviousDigit: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { previousDigitAlpha.animateTo(previousDigitAlphaDefault, tween(duration)) },
        )
    }

    val translateDownPreviousDigit: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { previousDigitTranslateAddition.animateTo(100f, tween(duration)) },
        )
    }

    val snapUpPreviousDigit: suspend CoroutineScope.() -> Unit = {
        awaitAll(
            async { previousDigitTranslateAddition.snapTo(previousDigitTranslateDefault) },
        )
    }

    val translateDownNextDigit: suspend CoroutineScope.(duration: Int) -> Unit = { duration ->
        awaitAll(
            async { nextDigitTranslateAddition.animateTo(100f, tween(duration)) },
        )
    }

    val snapUpNextDigit: suspend CoroutineScope.() -> Unit = {
        awaitAll(
            async { nextDigitTranslateAddition.snapTo(nextDigitTranslateDefault) },
        )
    }

    val previousLastDigit = lastDigit.value
    lastDigit.value = currentDigit

    LaunchedEffect(nextDigit, currentDigit, showNextDigitsIfNeeded) { // lastDigit.value,
        when {
            !showNextDigitsIfNeeded -> {
                moveToDefault(400)
            }
            nextDigit != currentDigit -> {
                awaitAll(
                    async { snapUpNextDigit() },
                    async { showNextDigitHalfVisibility(400) },
                    async { snapUpPreviousDigit() },
                    async { showPreviousDigit() },
                    async { fixedDigitAlpha.value = 0f },
                    async { fixedDigitText.value = currentDigit }
                )
                awaitAll(
                    async { translateDownNextDigit(400) },
                    async { showNextDigitFullVisibility(400) },
                    async { translateDownPreviousDigit(400) },
                    async { hidePreviousDigit(400) },
                    async { fixedDigitAlpha.value = 0f },
                    async { fixedDigitText.value = nextDigit }
                )
                awaitAll(
                    async { fixedDigitAlpha.value = 1f },
                    async { hideNextDigit() }
//                    async { snapUpNextDigit() },
//                    async { snapUpPreviousDigit() },
                )
//                hideNextDigit()
            }
//            previousLastDigit != currentDigit && nextDigit == currentDigit -> {
//                translateDownNextDigit(400)
//            }
//            previousLastDigit != currentDigit && nextDigit != currentDigit -> {
//                snapUpNextDigit(400)
//            }
        }

//        if (nextDigit == currentDigit || !showNextDigitsIfNeeded) {
//            isFixedDigitVisible.value = true
//            awaitAll(
//                async { nextDigitAlpha.animateTo(0f) },
//                async { nextDigitTranslateAddition.snapTo(0f) },
//                async { previousDigitAlpha.animateTo(0f) },
//                async { previousDigitTranslateAddition.snapTo(0f) },
//            )
//        } else {
//            isFixedDigitVisible.value = true
//            awaitAll(
//                async { nextDigitAlpha.animateTo(0.5f) },
//                async { nextDigitTranslateAddition.animateTo(24f) },
//                async { previousDigitAlpha.animateTo(0.5f) },
//                async { previousDigitTranslateAddition.animateTo(24f) },
//            )
//        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxHeight()
    ) {
        // Moving from top digit
        Text(
            text = nextDigit,
            color = whiteOriginal,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxHeight()
                .alpha(nextDigitAlpha.value)
                .translate(y = -24f + nextDigitTranslateAddition.value),
        )

        // Moving to bottom digit
        Text(
            text = currentDigit,
            color = whiteOriginal,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxHeight()
                .alpha(previousDigitAlpha.value)
                .translate(y = 76f + previousDigitTranslateAddition.value),
        )

        // Fixed digit
        Text(
            text = fixedDigitText.value,
            color = whiteOriginal,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            modifier = Modifier
                .height(45.dp)
                .alpha(fixedDigitAlpha.value),
        )
    }
}

private val defaultFontSize = 40.sp
