package com.erica.metas.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.erica.metas.utils.CurrencyUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SalesViewModel : ViewModel() {

    private val _goals = MutableStateFlow(GoalSettings())
    val goals: StateFlow<GoalSettings> = _goals.asStateFlow()

    private val _daysList = MutableStateFlow<List<DaySale>>(emptyList())
    val daysList: StateFlow<List<DaySale>> = _daysList.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _currentCalendar = MutableStateFlow(Calendar.getInstance())
    val currentCalendar: StateFlow<Calendar> = _currentCalendar.asStateFlow()

    private val _showConfetti = MutableStateFlow(false)
    val showConfetti: StateFlow<Boolean> = _showConfetti.asStateFlow()

    private var metaCelebrated = false
    private var superMetaCelebrated = false

    private var sharedPreferences: SharedPreferences? = null

    fun initPreferences(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("app_erica_prefs", Context.MODE_PRIVATE)
            loadSavedData()
        }
    }

    init {
        generateMonthDays()
    }

    fun previousMonth() {
        val cal = _currentCalendar.value.clone() as Calendar
        cal.add(Calendar.MONTH, -1)
        _currentCalendar.value = cal
        metaCelebrated = false
        superMetaCelebrated = false
        generateMonthDays()
        loadSavedData()
    }

    fun nextMonth() {
        val cal = _currentCalendar.value.clone() as Calendar
        cal.add(Calendar.MONTH, 1)
        _currentCalendar.value = cal
        metaCelebrated = false
        superMetaCelebrated = false
        generateMonthDays()
        loadSavedData()
    }

    fun getMonthYearLabel(): String {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))
        val name = formatter.format(_currentCalendar.value.time)
        return name.substring(0, 1).uppercase(Locale("pt", "BR")) + name.substring(1)
    }

    private fun getMonthKeyPrefix(): String {
        val year = _currentCalendar.value.get(Calendar.YEAR)
        val month = _currentCalendar.value.get(Calendar.MONTH) + 1
        return "${year}_${month}"
    }

    private fun generateMonthDays() {
        val calendar = _currentCalendar.value
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val list = mutableListOf<DaySale>()
        val dayNames = arrayOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")

        for (day in 1..daysInMonth) {
            val tempCal = Calendar.getInstance().apply {
                set(currentYear, currentMonth, day)
            }
            val dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK)
            val isSunday = (dayOfWeek == Calendar.SUNDAY)
            val dayName = dayNames[dayOfWeek - 1]

            list.add(
                DaySale(
                    dayNumber = day,
                    dayOfWeekName = dayName,
                    isSunday = isSunday,
                    isHoliday = false,
                    amount = 0.0,
                    digits = "0"
                )
            )
        }
        _daysList.value = list
    }

    fun updateDayAmount(dayNumber: Int, rawDigits: String) {
        val cleanDigits = rawDigits.replace(Regex("[^0-9]"), "")
        val amount = CurrencyUtils.parsePixDigitsToDouble(cleanDigits)

        _daysList.value = _daysList.value.map { day ->
            if (day.dayNumber == dayNumber) {
                day.copy(amount = amount, digits = if (cleanDigits.isEmpty()) "0" else cleanDigits)
            } else {
                day
            }
        }
        checkConfettiTrigger()
        saveData()
    }

    fun toggleHoliday(dayNumber: Int, isHoliday: Boolean) {
        _daysList.value = _daysList.value.map { day ->
            if (day.dayNumber == dayNumber) {
                day.copy(isHoliday = isHoliday)
            } else {
                day
            }
        }
        saveData()
    }

    fun updateGoals(metaDigits: String, superMetaDigits: String) {
        val metaVal = CurrencyUtils.parsePixDigitsToDouble(metaDigits)
        val superMetaVal = CurrencyUtils.parsePixDigitsToDouble(superMetaDigits)

        _goals.value = _goals.value.copy(
            meta = if (metaVal > 0) metaVal else 100000.0,
            superMeta = if (superMetaVal > 0) superMetaVal else 150000.0
        )
        checkConfettiTrigger()
        saveData()
    }

    fun updateUserName(newName: String) {
        if (newName.isNotBlank()) {
            _goals.value = _goals.value.copy(userName = newName)
            saveData()
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        saveData()
    }

    fun dismissConfetti() {
        _showConfetti.value = false
    }

    private fun checkConfettiTrigger() {
        val total = getTotalSales()
        val meta = _goals.value.meta
        val superMeta = _goals.value.superMeta

        if (total >= meta && !metaCelebrated) {
            metaCelebrated = true
            _showConfetti.value = true
        } else if (total >= superMeta && !superMetaCelebrated) {
            superMetaCelebrated = true
            _showConfetti.value = true
        }
    }

    fun getTotalSales(): Double {
        return _daysList.value.sumOf { it.amount }
    }

    fun isSuperMetaActive(): Boolean {
        return getTotalSales() >= _goals.value.meta
    }

    fun getActiveTargetValue(): Double {
        return if (isSuperMetaActive()) _goals.value.superMeta else _goals.value.meta
    }

    fun getActiveTargetPercentage(): Double {
        val target = getActiveTargetValue()
        if (target <= 0) return 0.0
        val percentage = (getTotalSales() / target) * 100.0
        return Math.min(percentage, 999.9)
    }

    fun getRemainingWorkingDays(): Int {
        val todayCal = Calendar.getInstance()
        val isCurrentMonth = todayCal.get(Calendar.YEAR) == _currentCalendar.value.get(Calendar.YEAR) &&
                todayCal.get(Calendar.MONTH) == _currentCalendar.value.get(Calendar.MONTH)
        val todayDayNumber = if (isCurrentMonth) todayCal.get(Calendar.DAY_OF_MONTH) else 1

        return _daysList.value.count { day ->
            day.dayNumber >= todayDayNumber && !day.isSunday && !day.isHoliday
        }
    }

    fun getTotalWorkingDaysInMonth(): Int {
        return _daysList.value.count { !it.isSunday && !it.isHoliday }
    }

    /**
     * Regra de Cálculo Atualizada:
     * Considera todos os dias restantes do mês, exceto domingos e feriados marcados.
     * Fórmula: (Meta / Super Meta - Total de Vendas) / (Dias úteis e não feriados restantes do mês)
     */
    fun getRequiredDailyAverage(): Double {
        val totalSales = getTotalSales()
        val currentTarget = getActiveTargetValue()
        val remainingTarget = currentTarget - totalSales

        if (remainingTarget <= 0) return 0.0

        val remainingWorkingDays = getRemainingWorkingDays()

        return if (remainingWorkingDays > 0) {
            remainingTarget / remainingWorkingDays
        } else {
            val totalWorkingDaysInMonth = getTotalWorkingDaysInMonth()
            if (totalWorkingDaysInMonth > 0) remainingTarget / totalWorkingDaysInMonth else 0.0
        }
    }


    private fun saveData() {
        val prefs = sharedPreferences ?: return
        val editor = prefs.edit()
        val prefix = getMonthKeyPrefix()

        editor.putFloat("meta", _goals.value.meta.toFloat())
        editor.putFloat("super_meta", _goals.value.superMeta.toFloat())
        editor.putString("user_name", _goals.value.userName)
        editor.putString("theme_mode", _themeMode.value.name)

        _daysList.value.forEach { day ->
            editor.putFloat("day_${prefix}_${day.dayNumber}", day.amount.toFloat())
            editor.putString("day_digits_${prefix}_${day.dayNumber}", day.digits)
            editor.putBoolean("day_holiday_${prefix}_${day.dayNumber}", day.isHoliday)
        }
        editor.apply()
    }

    private fun loadSavedData() {
        val prefs = sharedPreferences ?: return
        val prefix = getMonthKeyPrefix()

        val meta = prefs.getFloat("meta", 100000.0f).toDouble()
        val superMeta = prefs.getFloat("super_meta", 150000.0f).toDouble()
        val userName = prefs.getString("user_name", "Érica") ?: "Érica"
        val themeName = prefs.getString("theme_mode", ThemeMode.LIGHT.name) ?: ThemeMode.LIGHT.name

        _goals.value = GoalSettings(meta = meta, superMeta = superMeta, userName = userName)
        _themeMode.value = try { ThemeMode.valueOf(themeName) } catch (e: Exception) { ThemeMode.LIGHT }

        val currentList = _daysList.value.map { day ->
            val savedAmount = prefs.getFloat("day_${prefix}_${day.dayNumber}", 0.0f).toDouble()
            val savedDigits = prefs.getString("day_digits_${prefix}_${day.dayNumber}", "0") ?: "0"
            val savedHoliday = prefs.getBoolean("day_holiday_${prefix}_${day.dayNumber}", false)
            day.copy(amount = savedAmount, digits = savedDigits, isHoliday = savedHoliday)
        }
        _daysList.value = currentList
    }
}
