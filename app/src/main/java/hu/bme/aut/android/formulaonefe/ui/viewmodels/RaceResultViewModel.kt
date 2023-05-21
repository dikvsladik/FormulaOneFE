package hu.bme.aut.android.formulaonefe.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import hu.bme.aut.android.formulaonefe.data.result.Result

@RequiresApi(Build.VERSION_CODES.O)
class RaceResultViewModel : ViewModel() {
    val showYearPicker = mutableStateOf(false)
    val repository = FormulaRepository(ApiClient.api)
    val resultLists = mutableStateOf(listOf<Result>())
    val yearPicked = mutableStateOf(LocalDate.now().year)

    fun loadResults(selectedYear: Int) {
        viewModelScope.launch {
            val schedule = repository.getSchedule(selectedYear.toString())
            val totalRaces = schedule?.MRData?.total?.toIntOrNull() ?: 0
            val newResultLists = mutableListOf<Result>()
            yearPicked.value = selectedYear
            for (race in 1..totalRaces) {
                val result = repository.getResult(year = selectedYear.toString(), race = race.toString())
                result?.MRData?.let { resultList ->
                    newResultLists.add(Result(resultList))
                }
            }
            resultLists.value = newResultLists
        }
    }
}

