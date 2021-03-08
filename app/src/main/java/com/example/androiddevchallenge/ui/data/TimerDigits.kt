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
package com.example.androiddevchallenge.ui.data

import org.threeten.bp.LocalTime

data class TimerDigits(
    val digits: List<Digit> = emptyList(),
) {

    companion object {
        const val SECONDS_DIVIDER = 60
        const val SECONDS_MULTIPLIER = 10
        const val MINUTES_DIVIDER = 60
        const val MINUTES_MULTIPLIER = 10
        const val HOURS_MULTIPLIER = 10
    }

    fun addDigit(digit: Digit) = copy(digits = listOf(digit) + digits)

    fun isPossibleToAddNextDigit() = biggerHoursDigit == Digit.ZERO

    fun removeLastDigit(): TimerDigits {
        if (digits.isEmpty()) {
            return this
        }
        return copy(digits = digits.subList(1, digits.size))
    }

    fun transformDigitsToValidTime() = copy(digits = transformTimeToDigits(time))

    fun nextDigits() = minusOneSecond()

    fun minusOneSecond() = copy(digits = transformTimeToDigits(time.minusSeconds(1)))

    val isFinished get() = time.second == 0 && time.minute == 0 && time.hour == 0

    val smallerSecondsDigit get() = digits.getOrNull(0) ?: Digit.ZERO

    val biggerSecondsDigit get() = digits.getOrNull(1) ?: Digit.ZERO

    val smallerMinutesDigit get() = digits.getOrNull(2) ?: Digit.ZERO

    val biggerMinutesDigit get() = digits.getOrNull(3) ?: Digit.ZERO

    val smallerHoursDigit get() = digits.getOrNull(4) ?: Digit.ZERO

    val biggerHoursDigit get() = digits.getOrNull(5) ?: Digit.ZERO

    private val time get() = run {
        val smallerSeconds = smallerSecondsDigit.intValue
        val biggerSeconds = biggerSecondsDigit.intValue * SECONDS_MULTIPLIER
        val seconds = biggerSeconds + smallerSeconds
        val remainingSeconds = seconds % SECONDS_DIVIDER
        val dividedSeconds = seconds / SECONDS_DIVIDER

        val smallerMinutes = smallerMinutesDigit.intValue
        val biggerMinutes = biggerMinutesDigit.intValue * MINUTES_MULTIPLIER
        val minutes = biggerMinutes + smallerMinutes + dividedSeconds
        val remainingMinutes = minutes % MINUTES_DIVIDER
        val dividedMinutes = minutes / MINUTES_DIVIDER

        val smallerHours = smallerHoursDigit.intValue
        val biggerHours = biggerHoursDigit.intValue * HOURS_MULTIPLIER
        val hours = biggerHours + smallerHours + dividedMinutes

        LocalTime.of(hours, remainingMinutes, remainingSeconds)
    }

    private fun transformTimeToDigits(time: LocalTime): List<Digit> {
        return mutableListOf<Digit>().apply {
            add(index = 0, Digit.from(time.second % SECONDS_MULTIPLIER))
            add(index = 1, Digit.from(time.second / SECONDS_MULTIPLIER))
            add(index = 2, Digit.from(time.minute % MINUTES_MULTIPLIER))
            add(index = 3, Digit.from(time.minute / MINUTES_MULTIPLIER))
            add(index = 4, Digit.from(time.hour % HOURS_MULTIPLIER))
            add(index = 5, Digit.from(time.hour / HOURS_MULTIPLIER))
        }.toList()
    }
}
