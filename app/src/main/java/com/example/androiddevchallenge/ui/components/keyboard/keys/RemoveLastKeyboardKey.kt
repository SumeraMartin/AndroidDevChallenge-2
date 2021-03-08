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
package com.example.androiddevchallenge.ui.components.keyboard.keys

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RemoveLastKeyboardKey(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = defaultIconSize,
) {
    KeyboardKey(
        isVisible = isVisible,
        onClick = onClick,
        modifier = modifier,
    ) { color ->
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "",
            tint = color,
            modifier = Modifier
        )
    }
}

private val defaultRippleRadius = 40.dp
private val defaultIconSize = 24.dp
