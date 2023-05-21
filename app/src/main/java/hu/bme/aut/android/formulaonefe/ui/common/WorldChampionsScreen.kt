package hu.bme.aut.android.formulaonefe.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.android.formulaonefe.data.worldchampoins.Champions

@Composable
fun WorldChampionsScreen(champions: Champions?) {
    val championsList = remember { mutableStateOf(champions)}

}
