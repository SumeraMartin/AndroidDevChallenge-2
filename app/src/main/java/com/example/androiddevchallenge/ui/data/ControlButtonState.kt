package com.example.androiddevchallenge.ui.data

data class ControlButtonState(
    val isPlayButtonVisible: Boolean,
    val isPauseButtonVisible: Boolean,
    val isStopButtonVisible: Boolean,
) {

    companion object {
        val DEFAULT = ControlButtonState(
            isPlayButtonVisible = true,
            isPauseButtonVisible = false,
            isStopButtonVisible = false,
        )
    }

    fun nextStateAfterOnPlayClicked() = copy(
        isPlayButtonVisible = false,
        isPauseButtonVisible = true,
        isStopButtonVisible = true,
    )

    fun nextStateAfterOnPauseClicked() = copy(
        isPlayButtonVisible = true,
        isPauseButtonVisible = false,
        isStopButtonVisible = true,
    )

    fun nextStateAfterOnStopClicked() = copy(
        isPlayButtonVisible = true,
        isPauseButtonVisible = false,
        isStopButtonVisible = false,
    )
}
