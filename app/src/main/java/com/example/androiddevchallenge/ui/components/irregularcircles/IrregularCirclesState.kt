package com.example.androiddevchallenge.ui.components.irregularcircles

data class IrregularCirclesState(
    val eventId: Int,
    val eventEffect: IrregularCirclesEffect,
) {
    companion object {
        val DEFAULT = IrregularCirclesState(
            eventId = 0,
            eventEffect = IrregularCirclesEffect.Idle,
        )
    }
}

sealed class IrregularCirclesEffect {

    object Idle : IrregularCirclesEffect()

    data class Vibrate(val duration: Int) : IrregularCirclesEffect()

    data class VibrateOnceAndReturnBack(val durationToVibrate: Int, val durationToBack: Int) : IrregularCirclesEffect()

    data class Light(val duration: Int, val durationToBack: Int) : IrregularCirclesEffect()

    data class ReturnBack(val durationToBack: Int) : IrregularCirclesEffect()
}