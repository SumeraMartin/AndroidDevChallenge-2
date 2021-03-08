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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.data.TimerDigits
import com.example.androiddevchallenge.ui.utils.animation.floatOnBooleanAnimation

@Composable
fun CountdownDigits(
    digits: TimerDigits,
    nextDigits: TimerDigits,
    showNextDigitsIfNeeded: Boolean,
    showDescription: Boolean,
    modifier: Modifier = Modifier,
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.height(100.dp)
        ) {
            CountdownDigit(
                currentDigit = digits.biggerHoursDigit.stringValue,
                nextDigit = nextDigits.biggerHoursDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = digits.smallerHoursDigit.stringValue,
                nextDigit = nextDigits.smallerHoursDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = " : ",
                nextDigit = " : ",
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = digits.biggerMinutesDigit.stringValue,
                nextDigit = nextDigits.biggerMinutesDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = digits.smallerMinutesDigit.stringValue,
                nextDigit = nextDigits.smallerMinutesDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = " : ",
                nextDigit = " : ",
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = digits.biggerSecondsDigit.stringValue,
                nextDigit = nextDigits.biggerSecondsDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
            CountdownDigit(
                currentDigit = digits.smallerSecondsDigit.stringValue,
                nextDigit = nextDigits.smallerSecondsDigit.stringValue,
                showNextDigitsIfNeeded = showNextDigitsIfNeeded,
            )
        }
        val alpha = floatOnBooleanAnimation(0f, 1f, showDescription)
        Row(modifier = Modifier.padding(top = 80.dp).alpha(alpha.value)) {
            Text("H", color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.width(40.dp))
            Text("M", color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.width(94.dp))
            Text("S", color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.width(40.dp))
        }
    }
}

@Preview
@Composable
fun CountdownDigitsPreview() {
    CountdownDigits(TimerDigits(emptyList()), TimerDigits(emptyList()), false, true)
}
