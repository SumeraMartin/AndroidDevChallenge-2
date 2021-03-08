package com.example.androiddevchallenge.ui.screens.timer

import com.example.androiddevchallenge.ui.components.irregularcircles.IrregularCirclesEffect
import com.example.androiddevchallenge.ui.components.irregularcircles.IrregularCirclesState
import com.example.androiddevchallenge.ui.data.ControlButtonState
import com.example.androiddevchallenge.ui.data.Digit
import com.example.androiddevchallenge.ui.data.TimerDigits

data class TimerScreenState(
    val digits: TimerDigits,
    val irregularCirclesState: IrregularCirclesState,
    val controlButtonState: ControlButtonState,
    val isKeyboardVisible: Boolean,
    val isTimerRunning: Boolean,
    val isConrolPanelExpanded: Boolean,
) {

    companion object {
        val DEFAULT = TimerScreenState(
            digits = TimerDigits(),
            irregularCirclesState = IrregularCirclesState.DEFAULT,
            controlButtonState = ControlButtonState.DEFAULT,
            isKeyboardVisible = true,
            isTimerRunning = false,
            isConrolPanelExpanded = false,
        )
    }

    fun nextStateAfterDigitClicked(digit: Digit, effectDuration: Int, returnBackDuration: Int) = copy(
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.Light(effectDuration, returnBackDuration)
        ),
        digits = digits.addDigit(digit)
    )

    fun nextStateAfterRemoveLastClicked(effectDuration: Int, returnBackDuration: Int) = copy(
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.Light(effectDuration, returnBackDuration)
        ),
        digits = digits.removeLastDigit()
    )

    fun nextStateAfterOnStartClicked(tickDuration: Int) = copy(
        digits = digits.transformDigitsToValidTime(),
        controlButtonState = controlButtonState.nextStateAfterOnPlayClicked(),
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.Vibrate(tickDuration)
        ),
        isKeyboardVisible = false,
        isTimerRunning = true,
        isConrolPanelExpanded = true,
    )

    fun nextStateAfterOnStopClicked(returnEffectDuration: Int) = copy(
        isKeyboardVisible = true,
        isTimerRunning = false,
        isConrolPanelExpanded = false,
        controlButtonState = controlButtonState.nextStateAfterOnStopClicked(),
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.ReturnBack(returnEffectDuration)
        ),
    )

    fun nextStateAfterOnPauseClicked(returnEffectDuration: Int) = copy(
        isKeyboardVisible = false,
        isTimerRunning = false,
        controlButtonState = controlButtonState.nextStateAfterOnPauseClicked(),
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.ReturnBack(returnEffectDuration)
        ),
    )

    fun nextStateAfterTick(tickDuration: Int) = copy(
        digits = digits.minusOneSecond(),
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.Vibrate(tickDuration)
        ),
    )

    fun nextStateAfterFinished(returnEffectDuration: Int) = copy(
        controlButtonState = controlButtonState.nextStateAfterOnStopClicked(),
        irregularCirclesState = irregularCirclesState.copy(
            eventId = irregularCirclesState.eventId + 1,
            eventEffect = IrregularCirclesEffect.ReturnBack(returnEffectDuration)
        ),
        isKeyboardVisible = true,
        isTimerRunning = false,
        isConrolPanelExpanded = false,
    )
}
