package ru.kulikov.statistic.domain.use_cases.impl

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.domain.IndicatorValue
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.use_cases.ConvertStatisticToBarDataUseCase
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class ConvertStatisticToBarDataUseCaseImpl @Inject constructor() : ConvertStatisticToBarDataUseCase {
    override fun invoke(statistic: Statistic, periodMode: PeriodMode): Pair<BarData, ValueFormatter> {
        val values = statistic.indicatorValues
        return when (periodMode) {

            PeriodMode.WEEK -> {
                val formatter = WeekFormatter()
                val entries = buildWeekEntries(values)
                BarData(BarDataSet(entries, "Week")).apply {
                    setDrawValues(false)
                } to formatter
            }

            PeriodMode.MONTH -> {
                val formatter = MonthWeeksFormatter()
                val entries = buildMonthEntries(values)
                BarData(BarDataSet(entries, "Month")).apply {
                    setDrawValues(false)
                } to formatter
            }

            PeriodMode.YEAR -> {
                val formatter = YearFormatter()
                val entries = buildYearEntries(values)
                BarData(BarDataSet(entries, "Year")).apply {
                    setDrawValues(false)
                } to formatter
            }
        }
    }

    private fun buildWeekEntries(values: List<IndicatorValue>): List<BarEntry> {
        val map = values.associateBy { it.date }

        val entries = mutableListOf<BarEntry>()

        val monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY)

        for (i in 0 until 7) {
            val date = monday.plusDays(i.toLong())
            val dayValue = map[date]?.indicators?.map { it.percent }?.average() ?: 0.0
            entries += BarEntry(i.toFloat(), dayValue.toFloat())
        }
        return entries
    }

    private fun buildMonthEntries(values: List<IndicatorValue>): List<BarEntry> {
        if (values.isEmpty()) return emptyList()

        val anyDate = values.first().date
        val year = anyDate.year
        val month = anyDate.month

        val weekField = WeekFields.ISO.weekOfMonth()

        val firstDay = LocalDate.of(year, month, 1)
        val lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth())

        val minWeek = firstDay.get(weekField)
        val maxWeek = lastDay.get(weekField)

        val weekRange = minWeek..maxWeek

        val grouped = values.groupBy { it.date.get(weekField) }

        val entries = mutableListOf<BarEntry>()

        for (week in weekRange) {
            val list = grouped[week].orEmpty()

            val avg = list
                .flatMap { it.indicators }
                .map { it.percent }
                .average()
                .takeIf { !it.isNaN() } ?: 0.0

            entries += BarEntry((week - minWeek).toFloat(), avg.toFloat())
        }

        return entries
    }

    private fun buildYearEntries(values: List<IndicatorValue>): List<BarEntry> {
        val grouped = values.groupBy { it.date.monthValue }

        val entries = mutableListOf<BarEntry>()

        for (month in 1..12) {
            val list = grouped[month]
            val avg = list?.flatMap { it.indicators }
                ?.map { it.percent }
                ?.average()
                ?.takeIf { !it.isNaN() } ?: 0.0

            entries += BarEntry((month - 1).toFloat(), avg.toFloat())
        }

        return entries
    }

    class WeekFormatter : ValueFormatter() {
        private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val i = value.toInt()
            return days.getOrNull(i) ?: ""
        }
    }

    class MonthWeeksFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return "W${value.toInt() + 1}"
        }
    }

    class YearFormatter : ValueFormatter() {
        private val months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return months.getOrNull(value.toInt()) ?: ""
        }
    }
}