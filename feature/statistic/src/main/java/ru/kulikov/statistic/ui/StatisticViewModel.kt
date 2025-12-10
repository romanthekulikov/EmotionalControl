package ru.kulikov.statistic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.di.StatisticComponent
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticContract
import ru.kulikov.statistic.domain.use_cases.ConvertStatisticToBarDataUseCase
import ru.kulikov.statistic.domain.use_cases.GetNextReferenceDateUseCase
import ru.kulikov.statistic.domain.use_cases.GetPeriodUseCase
import ru.kulikov.statistic.domain.use_cases.GetPreviousReferenceDayUseCase
import ru.kulikov.statistic.domain.use_cases.GetStatisticByPeriodUseCase
import java.time.LocalDate
import javax.inject.Inject

class StatisticViewModel : StatisticStateHandler(), StatisticContract {

    @Inject
    lateinit var getPeriodUseCase: GetPeriodUseCase

    @Inject
    lateinit var getStatisticByPeriodUseCase: GetStatisticByPeriodUseCase

    @Inject
    lateinit var convertStatisticToBarDataUseCase: ConvertStatisticToBarDataUseCase

    @Inject
    lateinit var getNextReferenceDateUseCase: GetNextReferenceDateUseCase

    @Inject
    lateinit var getPreviousReferenceDayUseCase: GetPreviousReferenceDayUseCase

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()
    var referenceLocalDate: LocalDate = LocalDate.now()

    private var currentPage: Int = 0

    init {
        StatisticComponent.getInstance().inject(this)
        loadData()
    }

    override fun loadData(onDataLoad: () -> Unit) {
        viewModelScope.launch {
            _events.emit(UiEvent.InProgress)
            val period = getPeriodUseCase(referenceLocalDate, _state.value.mode)
            handleGetStatisticResult(getStatisticByPeriodUseCase(period.first, period.second))?.let {
                val data = convertStatisticToBarDataUseCase(it, _state.value.mode)
                setStatisticEntries(data)
                onDataLoad()
            }
            _events.emit(UiEvent.OutProgress)
        }

    }

    override fun moveForward() {
        referenceLocalDate = getNextReferenceDateUseCase(referenceLocalDate, _state.value.mode)
        loadData {
            currentPage++
        }
    }

    override fun moveBack() {
        if (currentPage < 1) {
            currentPage = 0
            return
        }

        referenceLocalDate = getPreviousReferenceDayUseCase(referenceLocalDate, _state.value.mode)
        loadData {
            currentPage--
        }
    }

    override fun changeMode(periodMode: PeriodMode) {
        setMode(periodMode)
        referenceLocalDate = LocalDate.now()
        currentPage = 0
        loadData()
    }

    private suspend fun handleGetStatisticResult(result: Result<Statistic>): Statistic? {
        when (result) {
            is Result.Failure<Statistic> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<Statistic> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
            is Result.Success<Statistic> -> return if (result.data.indicatorValues.isNotEmpty()) result.data else null
        }

        return null
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StatisticViewModel() as T
        }
    }
}