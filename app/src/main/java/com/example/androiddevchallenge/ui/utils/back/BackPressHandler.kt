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
