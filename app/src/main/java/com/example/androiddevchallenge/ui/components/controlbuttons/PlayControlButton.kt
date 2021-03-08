package com.example.androiddevchallenge.ui.components.controlbuttons

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlayControlButton(
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
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = "",
            tint = color,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun PlayControlButtonPreview() {
    PlayControlButton(
        onClick = {},
        isVisible = true
    )
}
