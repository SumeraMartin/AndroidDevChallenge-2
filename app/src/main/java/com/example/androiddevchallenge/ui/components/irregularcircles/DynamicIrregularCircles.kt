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
package com.example.androiddevchallenge.ui.components.irregularcircles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.blueAzure
import com.example.androiddevchallenge.ui.theme.blueSunny
import com.example.androiddevchallenge.ui.theme.purpleSnake

@Composable
fun DynamicIrregularCircles(
    circleColors: List<Color>,
    lightTopCircleColor: Color,
    state: IrregularCirclesState,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        circleColors.forEach { color ->
            DynamicIrregularCircle(
                circleColor = color,
                state = state,
                reactToLightEffect = false,
                modifier = Modifier.fillMaxSize()
            )
        }
        DynamicIrregularCircle(
            circleColor = lightTopCircleColor.copy(alpha = 0f),
            state = state,
            reactToLightEffect = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(widthDp = 360, heightDp = 360)
@Composable
fun DynamicIrregularCirclesPreview() {
    DynamicIrregularCircles(
        circleColors = listOf(purpleSnake, blueAzure, blueSunny),
        lightTopCircleColor = blueSunny,
        state = IrregularCirclesState(
            eventId = 0,
            eventEffect = IrregularCirclesEffect.Idle
        ),
    )
}
