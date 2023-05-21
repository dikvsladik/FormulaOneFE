package hu.bme.aut.android.formulaonefe.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.formulaonefe.data.worldchampoins.Champions

class WorldChampionsViewModel : ViewModel() {
    val champions = mutableStateOf<Champions?>(null)

    fun setChampions(champions: Champions) {
        this.champions.value = champions
    }
}

