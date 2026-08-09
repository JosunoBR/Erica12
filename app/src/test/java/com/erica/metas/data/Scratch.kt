package com.erica.metas.data

import java.util.Calendar

fun main() {
    val viewModel = SalesViewModel()
    viewModel.updateGoals("100000", "200000") // Meta 1000.00, Super Meta 2000.00
    
    val initialAvg = viewModel.getRequiredDailyAverage()
    println("Initial Avg (Sales=0, Meta=1000): $initialAvg")
    
    // Mark a future day as holiday
    val firstWorkingDay = viewModel.daysList.value.first { !it.isSunday }.dayNumber
    viewModel.toggleHoliday(firstWorkingDay, true)
    
    val afterHolidayAvg = viewModel.getRequiredDailyAverage()
    println("After Holiday Avg: $afterHolidayAvg")
    
    if (afterHolidayAvg > initialAvg) {
        println("SUCCESS: Holiday excluded from Meta calculation")
    } else {
        println("FAILURE: Holiday NOT excluded from Meta calculation")
    }
}
