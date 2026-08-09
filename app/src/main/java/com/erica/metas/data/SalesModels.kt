package com.erica.metas.data

data class DaySale(
    val dayNumber: Int,
    val dayOfWeekName: String,
    val isSunday: Boolean,
    val isHoliday: Boolean = false,
    val amount: Double = 0.0,
    val digits: String = "0"
)

data class GoalSettings(
    val meta: Double = 100000.0,
    val superMeta: Double = 150000.0,
    val userName: String = "Érica"
)

enum class ThemeMode {
    LIGHT,
    DARK
}
