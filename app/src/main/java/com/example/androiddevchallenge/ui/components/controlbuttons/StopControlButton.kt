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
package com.example.androiddevchallenge.ui.components.controlbuttons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StopControlButton(
    onClick: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    ControlButton(
        onClick = onClick,
        isVisible = isVisible,
        modifier = modifier,
    ) { color ->
        Icon(
            imageVector = Icons.Outlined.Stop,
            contentDescription = "",
            tint = color,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun StopControlButtonPreview() {
    StopControlButton(
        onClick = {},
        isVisible = true
    )
}
