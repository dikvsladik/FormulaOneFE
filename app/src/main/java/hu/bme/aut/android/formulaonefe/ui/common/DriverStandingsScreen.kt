package hu.bme.aut.android.formulaonefe.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import hu.bme.aut.android.formulaonefe.data.standings.DriverStanding
import hu.bme.aut.android.formulaonefe.data.standings.StandingsLists

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.InternalLandscapistApi
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.R
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.ui.tools.YearPickerScreen1950
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun DriverStandingsScreen(standingsLists: List<StandingsLists>,
                          lifecycleScope: LifecycleCoroutineScope
) {
    val showYearPicker = remember { mutableStateOf(false) }
    val repository = FormulaRepository(ApiClient.api)
    val updatedStandingsLists = remember { mutableStateOf(standingsLists) }
    val yearPicked = remember { mutableStateOf(2023)}

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showYearPicker.value) {
            YearPickerScreen1950 { selectedYear ->
                lifecycleScope.launch {
                    val newStandingsLists = repository.getFormula(selectedYear.toString())!!.MRData.StandingsTable.StandingsLists
                    yearPicked.value=selectedYear
                    updatedStandingsLists.value = newStandingsLists
                }
                showYearPicker.value = false
            }
        } else {
            updatedStandingsLists.value.forEach { standingsList ->
                DriverList(standingsList.DriverStandings,yearPicked.value)
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



@OptIn(ExperimentalPagerApi::class)
@Composable
fun DriverList(drivers: List<DriverStanding>, yearPicked: Int) {
    val pagerState = rememberPagerState()

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(), count = drivers.size) { page ->
        DriverRow(drivers[page],yearPicked)
    }
}

@OptIn(InternalLandscapistApi::class)
@Composable
fun DriverRow(driverStanding: DriverStanding, yearPicked: Int) {
    val givenName = driverStanding.Driver.givenName
    val familyName = driverStanding.Driver.familyName
    val initials = givenName.take(3) + familyName.take(3)
    val formattedName = "${givenName}_${familyName}".replace(" ", "_")

    val MyCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

    val positionColor = when (driverStanding.position) {
        "1" -> Color(0xFFD4AF37) // Gold
        "2" -> Color(0xFFC0C0C0) // Silver
        "3" -> Color(0xFFCD7F32) // Bronze
        else -> Color.Black
    }

    val constructorId = driverStanding.Constructors[0].constructorId

    val constructorIdpic = when(constructorId){
        "ferrari"->"https://media.formula1.com/content/dam/fom-website/teams/2023/ferrari-logo.png.transform/2col-retina/image.png"
        "alphatauri"->"https://media.formula1.com/content/dam/fom-website/teams/2023/alphatauri-logo.png.transform/2col-retina/image.png"
        "mercedes"->"https://media.formula1.com/content/dam/fom-website/teams/2023/mercedes-logo.png.transform/2col-retina/image.png"
        "red_bull"->"https://media.formula1.com/content/dam/fom-website/teams/2023/red-bull-racing-logo.png.transform/2col-retina/image.png"
        "haas" ->"https://media.formula1.com/content/dam/fom-website/teams/2023/haas-f1-team-logo.png.transform/2col-retina/image.png"
        "alfa"->"https://media.formula1.com/content/dam/fom-website/teams/2023/alfa-romeo-logo.png.transform/2col-retina/image.png"
        "alpine"->"https://media.formula1.com/content/dam/fom-website/teams/2023/alpine-logo.png.transform/2col-retina/image.png"
        "aston_martin"->"https://media.formula1.com/content/dam/fom-website/teams/2023/aston-martin-logo.png.transform/2col-retina/image.png"
        "mclaren"->"https://media.formula1.com/content/dam/fom-website/teams/2023/mclaren-logo.png.transform/2col-retina/image.png"
        "williams"->"https://media.formula1.com/content/dam/fom-website/teams/2023/williams-logo.png.transform/2col-retina/image.png"

        else-> "https://www.formula1.com/etc/designs/fom-website/images/fia_logo.png"
    }
    val imageUrl = "https://example.com/images/$constructorId" // Replace with the actual URL for the image
    Surface(
        shape = RoundedCornerShape(16.dp), // Lekerekítés mértéke
        color = Color.White, // Fehér háttér
        modifier = Modifier
            .padding(16.dp)
            .wrapContentWidth()
    ) {
        Text(
            text = yearPicked.toString(),
            color = Color(0xFFDC0000),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MyCustomFont,
            modifier = Modifier.padding(16.dp),
            style = TextStyle(
                fontSize = 30.sp,
                shadow = Shadow(color = Color.Black, blurRadius = 3f)
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = driverStanding.position,
                color = positionColor,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MyCustomFont
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${driverStanding.Driver.givenName} ${driverStanding.Driver.familyName}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MyCustomFont
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                com.skydoves.landscapist.glide.GlideImage(imageModel = {
                    "https://media.formula1.com/content/dam/fom-website/drivers/${
                        initials.first().uppercaseChar()
                    }/${initials.uppercase()}" + "01_$formattedName/${initials.lowercase()}01.png.transform/2col-retina/image.png"
                },
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
                        requestSize = IntSize(1000, 800),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.CenterStart

                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                )
                com.skydoves.landscapist.glide.GlideImage(imageModel = { constructorIdpic },
                    component = rememberImageComponent {
                        +CircularRevealPlugin()
                        +ShimmerPlugin(
                            baseColor = Color(0xFFC0C0C0),
                            highlightColor = Color(0xFFCD7F32)
                        )
                    },
                    failure = {
                        Text(text = "image request failed.")
                    },
                    imageOptions = ImageOptions(
                        requestSize = IntSize(200, 200),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = driverStanding.points,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MyCustomFont
            )
            if (driverStanding.wins != "0") {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Won ",
                        fontSize = 19 .sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MyCustomFont
                    )
                    Text(
                        text = driverStanding.wins,
                        fontSize = 22.sp, // A wins mérete
                        color = Color.Red, // A wins színe
                        fontWeight = FontWeight.Bold,
                        fontFamily = MyCustomFont
                    )
                    val raceText = if (driverStanding.wins.toInt() == 1) " race" else " races"
                    Text(
                        text = "$raceText in the season",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MyCustomFont
                    )
                }

            }
        }
    }
}



