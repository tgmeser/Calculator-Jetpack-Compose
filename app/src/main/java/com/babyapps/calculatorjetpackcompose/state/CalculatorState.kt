package com.babyapps.calculatorjetpackcompose.state

import com.babyapps.calculatorjetpackcompose.action.CalculatorOperation

data class CalculatorState(
    val firstNum: String = "",
    val secondNum: String = "",
    val calculatorOperation: CalculatorOperation? = null
)