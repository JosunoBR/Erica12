package com.erica.metas.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class SalesViewModelTest {

    private lateinit var viewModel: SalesViewModel

    @Before
    fun setup() {
        viewModel = SalesViewModel()
        // Reset goals to known values
        viewModel.updateGoals("100000", "200000") // Meta 1000.00, Super Meta 2000.00
    }

    @Test
    fun `test holiday exclusion with zero sales (Targeting Meta)`() {
        val initialAvg = viewModel.getRequiredDailyAverage()
        
        // Mark the first non-Sunday day as holiday
        val firstWorkingDay = viewModel.daysList.value.first { !it.isSunday }.dayNumber
        viewModel.toggleHoliday(firstWorkingDay, true)
        
        val afterHolidayAvg = viewModel.getRequiredDailyAverage()
        
        assertNotEquals("Average should change when a holiday is added", initialAvg, afterHolidayAvg)
        assert(afterHolidayAvg > initialAvg) { "Average should increase when working days decrease" }
    }

    @Test
    fun `test holiday exclusion with partial sales (Targeting Meta)`() {
        // Set sales to 500 (Meta is 1000)
        val firstWorkingDay = viewModel.daysList.value.first { !it.isSunday }.dayNumber
        viewModel.updateDayAmount(firstWorkingDay, "50000")
        
        val initialAvg = viewModel.getRequiredDailyAverage()
        
        // Mark another day as holiday
        val secondWorkingDay = viewModel.daysList.value.filter { !it.isSunday && it.dayNumber != firstWorkingDay }.first().dayNumber
        viewModel.toggleHoliday(secondWorkingDay, true)
        
        val afterHolidayAvg = viewModel.getRequiredDailyAverage()
        
        assertNotEquals("Average should change when a holiday is added", initialAvg, afterHolidayAvg)
        assert(afterHolidayAvg > initialAvg) { "Average should increase when working days decrease" }
    }

    @Test
    fun `test holiday exclusion when Super Meta is active`() {
        // Set sales to 1100 (Meta is 1000, so Super Meta is active)
        val firstWorkingDay = viewModel.daysList.value.first { !it.isSunday }.dayNumber
        viewModel.updateDayAmount(firstWorkingDay, "110000")
        
        assert(viewModel.isSuperMetaActive())
        
        val initialAvg = viewModel.getRequiredDailyAverage()
        
        // Mark another day as holiday
        val secondWorkingDay = viewModel.daysList.value.filter { !it.isSunday && it.dayNumber != firstWorkingDay }.first().dayNumber
        viewModel.toggleHoliday(secondWorkingDay, true)
        
        val afterHolidayAvg = viewModel.getRequiredDailyAverage()
        
        assertNotEquals("Average should change when a holiday is added", initialAvg, afterHolidayAvg)
        assert(afterHolidayAvg > initialAvg) { "Average should increase when working days decrease" }
    }
}
