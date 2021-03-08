package com.example.androiddevchallenge.ui.utils.remember

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun rememberAnimatable(initialValue: Float) = remember { Animatable(initialValue) }

@Composable
fun rememberAnimatable(initialValue: Color) = remember { androidx.compose.animation.Animatable(initialValue) }
