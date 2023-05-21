package hu.bme.aut.android.formulaonefe.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.formulaonefe.data.driverinformation.DriverInformation
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DriverInformationViewModel : ViewModel() {
    private val repository = FormulaRepository(ApiClient.api)
    val driversList = mutableStateOf<DriverInformation?>(null)
    val showYearPicker = mutableStateOf(false)

    init {
        viewModelScope.launch {
            val currentYear = LocalDate.now().year
            driversList.value = repository.getDrivers(currentYear.toString())
        }
    }

    fun toggleYearPicker() {
        showYearPicker.value = !showYearPicker.value
    }

    fun updateDriversList(selectedYear: Int) {
        viewModelScope.launch {
            val newDriversList = repository.getDrivers(selectedYear.toString())
            driversList.value = newDriversList
        }
    }
}
