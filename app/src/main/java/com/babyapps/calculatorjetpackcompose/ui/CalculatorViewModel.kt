package com.babyapps.calculatorjetpackcompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.babyapps.calculatorjetpackcompose.action.CalculatorAction
import com.babyapps.calculatorjetpackcompose.action.CalculatorOperation
import com.babyapps.calculatorjetpackcompose.state.CalculatorState

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> calculate(action)
            is CalculatorAction.Delete -> delete()
        }
    }

    private fun calculate(action: CalculatorAction.Calculate) {
        val firstNumber = state.firstNum.toDoubleOrNull()
        val secondNumber = state.secondNum.toDoubleOrNull()
        if (firstNumber != null && secondNumber != null) {
            val result = when (state.calculatorOperation) {
                is CalculatorOperation.Add -> firstNumber + secondNumber
                is CalculatorOperation.Multiply -> firstNumber * secondNumber
                is CalculatorOperation.Subtract -> firstNumber - secondNumber
                is CalculatorOperation.Divide -> firstNumber / secondNumber
                null -> return
            }
            state = state.copy(
                firstNum = result.toString().take(15),
                secondNum = "",
                calculatorOperation = null
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.firstNum.isNotBlank()) {
            state = state.copy(calculatorOperation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.calculatorOperation == null && !state.firstNum.contains(".") && state.firstNum.isNotBlank()) {
            state = state.copy(firstNum = state.firstNum + ".")
            return
        }
        if (!state.secondNum.contains(".") && state.secondNum.isNotBlank()) {
            state = state.copy(secondNum = state.secondNum + ".")
        }
    }

    private fun enterNumber(number: Int) {
        if (state.calculatorOperation == null) {
            if (state.firstNum.length >= MAX_NUMBER_LENGTH) {
                return
            }
            state = state.copy(
                firstNum = state.firstNum + number
            )
            return
        }
        if (state.secondNum.length >= MAX_NUMBER_LENGTH) {
            return
        }
        state = state.copy(
            secondNum = state.secondNum + number
        )

    }

    private fun delete() {
        when {
            state.secondNum.isNotBlank() -> state =
                state.copy(secondNum = state.secondNum.dropLast(1))

            state.calculatorOperation != null -> state = state.copy(calculatorOperation = null)
            state.firstNum.isNotBlank() -> state = state.copy(firstNum = state.firstNum.dropLast(1))
        }
    }

    companion object {
        private const val MAX_NUMBER_LENGTH = 8
    }
}