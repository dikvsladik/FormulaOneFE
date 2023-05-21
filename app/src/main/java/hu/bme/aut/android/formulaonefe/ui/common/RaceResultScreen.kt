package hu.bme.aut.android.formulaonefe.ui.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import hu.bme.aut.android.formulaonefe.data.result.Race


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import hu.bme.aut.android.formulaonefe.data.result.Result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.LifecycleCoroutineScope
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.R
import hu.bme.aut.android.formulaonefe.data.result.ResultX
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.ui.tools.YearPickerScreen1950
import kotlinx.coroutines.launch
import java.time.LocalDate

val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun RaceResultScreen(initialResultLists: List<Result>,lifecycleScope: LifecycleCoroutineScope) {


    val showYearPicker = remember { mutableStateOf(false) }
    val repository = FormulaRepository(ApiClient.api)
    val resultLists = remember { mutableStateOf(initialResultLists) }
    val yearPicked = remember { mutableStateOf(LocalDate.now().year) }
    val pageToShow = if (yearPicked.value == LocalDate.now().year){
        resultLists.value.size
    } else {
        0
    }

    val pagerState = rememberPagerState(initialPage = pageToShow)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showYearPicker.value) {
            YearPickerScreen1950 { selectedYear ->
                lifecycleScope.launch {
                    val schedule = repository.getSchedule(selectedYear.toString())
                    val totalRaces = schedule?.MRData?.total?.toIntOrNull() ?: 0
                    val newResultLists = mutableListOf<Result>()
                    yearPicked.value=selectedYear
                    pagerState.scrollToPage(0)
                    for (race in 1..totalRaces) {
                        val result = repository.getResult(year = selectedYear.toString(), race = race.toString())
                        result?.MRData?.let { resultList ->
                            newResultLists.add(Result(resultList))
                        }
                    }

                    resultLists.value = newResultLists
                }
                showYearPicker.value = false
            }
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentWidth()
            ) {
                HorizontalPager(state = pagerState, count = resultLists.value.size) { page ->
                    val race = resultLists.value[page].MRData.RaceTable.Races[0]
                    val countryFlag= when(race.Circuit.Location.country){
                        "USA"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/united-states-flag.png.transform/2col-retina/image.png "
                        "UK"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/great-britain-flag.png.transform/2col-retina/image.png"
                        "UAE"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/abu-dhabi-flag.png.transform/2col-retina/image.png"
                        else-> "https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/${race.Circuit.Location.country.lowercase().replace(" ","-")}-flag.png.transform/2col-retina/image.png"
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = yearPicked.value.toString(),
                                color = Color(0xFFDC0000),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = myCustomFont,
                                modifier = Modifier.padding(10.dp),
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    shadow = Shadow(color = Color.Black, blurRadius = 3f)
                                )
                            )
                            GlideImage(
                                imageModel = { countryFlag },
                                component = rememberImageComponent {
                                    +CircularRevealPlugin()
                                    +ShimmerPlugin(
                                        baseColor = Color(0xFFC0C0C0),
                                        highlightColor = Color(0xFFCD7F32)
                                    )
                                    +PlaceholderPlugin.Failure(painterResource(id = R.drawable.unknown))
                                },
                                failure = {
                                    Text(text = " ")
                                },
                                imageOptions = ImageOptions(
                                    requestSize = IntSize(206, 116),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center

                                ),
                                modifier = Modifier.padding(10.dp)

                                    .clip(RoundedCornerShape(2.dp))
                                    .border(2.dp, Color.Black, RoundedCornerShape(2.dp))
                            )
                            Text(
                                text = race.round+".",
                                color = Color.Black,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = myCustomFont,
                                modifier = Modifier.padding(10.dp),

                                style = TextStyle(
                                    fontSize = 30.sp,
                                    shadow = Shadow(color = Color.Black, blurRadius = 3f)
                                )
                            )
                        }
                        Text(text = race.raceName, fontFamily = myCustomFont,modifier = Modifier.padding(bottom = 20.dp), fontSize = 36.sp)
                        Row {
                            Column(modifier = Modifier.padding(end = 2.dp)) {
                                race.Results.forEach { result ->
                                    val positionColor = when (result.position) {
                                        "1" -> Color(0xFFD4AF37) // Gold
                                        "2" -> Color(0xFFC0C0C0) // Silver
                                        "3" -> Color(0xFFCD7F32) // Bronze
                                        else -> Color.Black
                                    }
                                    Text(
                                        text = result.position,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        fontFamily = myCustomFont,
                                        fontSize = 11.sp, color = positionColor
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(end = 2.dp)) {
                                race.Results.forEach { result ->
                                    Text(
                                        text = "${result.Driver.givenName} ${result.Driver.familyName}",
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        fontFamily = myCustomFont,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(end = 2.dp)) {
                                race.Results.forEach { result ->
                                    Text(
                                        text = result.grid,
                                        modifier = Modifier.padding(bottom = 10.dp),
                                        fontFamily = myCustomFont,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(end = 2.dp)) {
                                race.Results.forEach { result ->
                                    result.Time?.time?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            fontFamily = myCustomFont,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                            Column {
                                race.Results.forEach { result ->
                                    Text(
                                        text = result.status,
                                        modifier = Modifier.padding(bottom = 11.5.dp),
                                        fontFamily = myCustomFont,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showYearPicker.value = !showYearPicker.value },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = Color(0xFFDC0000)
        ) {
            Icon(Icons.Filled.DateRange, contentDescription = "Év kiválasztása")
        }
    }
}





