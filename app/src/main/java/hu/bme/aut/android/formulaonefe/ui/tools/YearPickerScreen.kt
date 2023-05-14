package hu.bme.aut.android.formulaonefe.ui.tools



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun YearPickerScreen(onYearSelected: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val startYear = 1950
        val endYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (endYear downTo startYear).toList()

        items(years.chunked(3)) { yearPair ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                yearPair.forEach { year ->
                    Button(
                        onClick = { onYearSelected(year) },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFDC0000))
                    ) {
                        Text(text = year.toString())
                    }
                }
            }
        }
    }
}
