package hu.bme.aut.android.formulaonefe.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.formulaonefe.data.standings.StandingsLists
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import kotlinx.coroutines.launch

class DriverStandingsViewModel : ViewModel() {
    private val repository = FormulaRepository(ApiClient.api)

    val showYearPicker = mutableStateOf(false)
    val updatedStandingsLists = mutableStateOf(emptyList<StandingsLists>())
    val yearPicked = mutableStateOf(2023)

    init {
        fetchDriverStandings(yearPicked.value)
    }

    fun fetchDriverStandings(year: Int) {
        viewModelScope.launch {
            val newStandingsLists = repository.getFormula(year.toString())!!.MRData.StandingsTable.StandingsLists
            yearPicked.value = year
            updatedStandingsLists.value = newStandingsLists
        }
    }

    fun toggleYearPicker() {
        showYearPicker.value = !showYearPicker.value
    }
}
