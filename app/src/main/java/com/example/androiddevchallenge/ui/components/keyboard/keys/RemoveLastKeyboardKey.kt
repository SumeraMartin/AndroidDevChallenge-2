package com.example.androiddevchallenge.ui.components.keyboard.keys

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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