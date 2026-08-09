package com.erica.metas.scratch

import com.erica.metas.data.SalesViewModel
import com.erica.metas.data.DaySale
import com.erica.metas.data.GoalSettings
import java.util.Calendar

fun main() {
    val vm = SalesViewModel()
    // Mock days for a 30-day month
    val days = (1..30).map { i ->
        DaySale(dayNumber = i, dayOfWeekName = "Day $i", isSunday = (i % 7 == 0), isHoliday = false, amount = 0.0)
    }
    
    // Set daysList directly via reflection or just use the VM methods
    // Since I can't easily set private fields, I'll use the VM methods
    
    println("--- Test 1: Zero Sales ---")
    val initialAvg = vm.getRequiredDailyAverage()
    println("Initial Avg: $initialAvg")
    
    vm.toggleHoliday(1, true)
    val afterHolidayAvg = vm.getRequiredDailyAverage()
    println("After Holiday (Day 1) Avg: $afterHolidayAvg")
    
    if (afterHolidayAvg > initialAvg) {
        println("SUCCESS: Average increased when holiday was added.")
    } else {
        println("FAILURE: Average did not change or decreased.")
    }
}
