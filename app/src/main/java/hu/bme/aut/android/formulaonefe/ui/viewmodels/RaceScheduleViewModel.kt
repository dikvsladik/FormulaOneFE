package hu.bme.aut.android.formulaonefe.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceTable

import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class RaceScheduleViewModel : ViewModel() {
    private val repository = FormulaRepository(ApiClient.api)
    private val _schedule = mutableStateOf<RaceTable?>(null)
    val schedule: State<RaceTable?> = _schedule

    init {
        viewModelScope.launch {
            val initialSchedule = repository.getSchedule(LocalDate.now().year.toString())!!.MRData.RaceTable
            _schedule.value = initialSchedule
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextRaceIndex(schedule: RaceTable): Int {
        return if (LocalDate.now().year == schedule.season.toInt()) {
            schedule.Races.indexOfFirst { race ->
                LocalDate.parse(race.date, DateTimeFormatter.ISO_LOCAL_DATE).isAfter(LocalDate.now())
            }.coerceAtLeast(0)
        } else {
            0
        }
    }

    suspend fun updateSchedule(year: Int) {
        val newSchedule = repository.getSchedule(year.toString())!!.MRData.RaceTable
        _schedule.value = newSchedule
    }
}
