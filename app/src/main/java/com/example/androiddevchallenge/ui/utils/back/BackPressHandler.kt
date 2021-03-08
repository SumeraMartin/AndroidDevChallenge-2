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
package com.example.androiddevchallenge.ui.utils.back

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun BackPressHandler(isActive: Boolean, onBackPressed: () -> Unit) {
    val currentOnBackPressed = rememberUpdatedState(onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed.value()
            }
        }
    }

    if (isActive) {
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current
        DisposableEffect(backDispatcher) {
            backDispatcher.onBackPressedDispatcher.addCallback(backCallback)

            onDispose {
                backCallback.remove()
            }
        }
    }
}
