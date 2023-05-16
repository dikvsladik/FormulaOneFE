package hu.bme.aut.android.formulaonefe.ui.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.bme.aut.android.formulaonefe.R
import hu.bme.aut.android.formulaonefe.data.raceschedule.Race
import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceTable
import hu.bme.aut.android.formulaonefe.notification.scheduleRaceStartReminder
import java.time.OffsetDateTime

import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.LifecycleCoroutineScope
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.ui.tools.YearPickerScreen2005
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaceScheduleScreen(schedule: RaceTable, lifecycleScope: LifecycleCoroutineScope) {
    val showYearPicker = remember { mutableStateOf(false) }
    val repository = FormulaRepository(ApiClient.api)
    val yearPicked = remember { mutableStateOf(2023) }
    val updatedSchedulesLists = remember { mutableStateOf(schedule) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showYearPicker.value) {
            YearPickerScreen2005 { selectedYear ->
                lifecycleScope.launch {
                    val newStandingsLists = repository.getSchedule(selectedYear.toString())!!.MRData.RaceTable
                    yearPicked.value=selectedYear
                    updatedSchedulesLists.value = newStandingsLists
                }
                showYearPicker.value = false
            }
        } else {
            VerticalPager(
                modifier = Modifier.fillMaxSize(),
                pageCount = updatedSchedulesLists.value.Races.size,
                contentPadding = PaddingValues(16.dp)
            ) { pageIndex ->
                val race = updatedSchedulesLists.value.Races[pageIndex]
                RaceItem(race,yearPicked.value)
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaceItem(race: Race, yearPicked: Int) {
    val context = LocalContext.current
    val raceDateTime = OffsetDateTime.parse("${race.date}T${race.time}").toLocalDateTime()
    val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

    val country= when(race.Circuit.Location.country){
        "UK"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Track%20icons%204x3/Great%20Britain%20carbon.png.transform/2col-retina/image.png"
        "UAE"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Track%20icons%204x3/Abu%20Dhabi%20carbon.png.transform/2col-retina/image.png"
        else-> "https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Track%20icons%204x3/${race.Circuit.Location.country.capitalize()}%20carbon.png.transform/2col-retina/image.png"
    }
    val countryFlag= when(race.Circuit.Location.country){
        "USA"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/united-states-flag.png.transform/2col-retina/image.png "
        "UK"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/great-britain-flag.png.transform/2col-retina/image.png"
        "UAE"->"https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/abu-dhabi-flag.png.transform/2col-retina/image.png"
        else-> "https://media.formula1.com/content/dam/fom-website/2018-redesign-assets/Flags%2016x9/${race.Circuit.Location.country.lowercase().replace(" ","-")}-flag.png.transform/2col-retina/image.png"
    }


    LaunchedEffect(race.raceName, raceDateTime) {
        scheduleRaceStartReminder(context, race.raceName, raceDateTime)
    }
    Surface(
        shape = RoundedCornerShape(16.dp), // Lekerekítés mértéke
        color = Color.White, // Fehér háttér
        modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(3.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = yearPicked.toString(),
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
            Spacer(modifier = Modifier.height(1.dp))
            Column(
                modifier = Modifier.padding(1.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(text = race.raceName, fontWeight = FontWeight.Bold, fontSize = 30.sp,fontFamily = myCustomFont,textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                Text(text = race.Circuit.circuitName, fontSize = 23.sp,fontFamily = myCustomFont,textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))

                GlideImage(
                    imageModel = { country },
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
                        requestSize = IntSize(700, 500),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center

                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                )
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault())


                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp).fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(2f).fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Megnevezések
                        race.FirstPractice?.let{
                            val firstPracticeLocalDateTime =
                                OffsetDateTime.parse("${race.FirstPractice.date}T${race.FirstPractice.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()

                            Text(text = "First Practice", fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.SecondPractice?.let{
                            Text(text = "Second Practice", fontSize = 14.sp, fontFamily = myCustomFont)
                        }


                        race.ThirdPractice?.let {
                            Text(text = "Third Practice", fontSize = 14.sp, fontFamily = myCustomFont)
                        }

                        race.Qualifying?.let{
                            val qualifyingLocalDateTime =
                                OffsetDateTime.parse("${race.Qualifying.date}T${race.Qualifying.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedQualifyingDateTime =
                                dateTimeFormatter.format(qualifyingLocalDateTime)
                            Text(text = "Qualifying", fontSize = 14.sp, fontFamily = myCustomFont)
                        }


                        race.Sprint?.let {
                            Text(text = "Sprint", fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.time?.let{
                            Text(text = "Race", fontSize = 14.sp, fontFamily = myCustomFont)
                        }

                    }

                    Column(
                        modifier = Modifier
                            .weight(1f).fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Hónap és nap
                        race.FirstPractice?.let{
                            val firstPracticeLocalDateTime =
                                OffsetDateTime.parse("${race.FirstPractice.date}T${race.FirstPractice.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedFirstPracticeDateTime =
                                dateTimeFormatter.format(firstPracticeLocalDateTime)
                            Text(text = formattedFirstPracticeDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                       race.SecondPractice?.let {
                           val secondPracticeLocalDateTime =
                               OffsetDateTime.parse("${race.SecondPractice.date}T${race.SecondPractice.time}")
                                   .atZoneSameInstant(ZoneId.systemDefault())
                                   .toLocalDateTime()
                           val formattedSecondPracticeDateTime =
                               dateTimeFormatter.format(secondPracticeLocalDateTime)
                           Text(text = formattedSecondPracticeDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                       }


                        race.ThirdPractice?.let {
                            val thirdPracticeLocalDateTime =
                                OffsetDateTime.parse("${it.date}T${it.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedThirdPracticeDateTime =
                                dateTimeFormatter.format(thirdPracticeLocalDateTime)
                            Text(text = formattedThirdPracticeDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.Qualifying?.let{
                            val qualifyingLocalDateTime =
                                OffsetDateTime.parse("${race.Qualifying.date}T${race.Qualifying.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedQualifyingDateTime =
                                dateTimeFormatter.format(qualifyingLocalDateTime)
                            Text(text = formattedQualifyingDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                        }


                        race.Sprint?.let {
                            val sprintLocalDateTime = OffsetDateTime.parse("${it.date}T${it.time}")
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                            val formattedSprintDateTime = dateTimeFormatter.format(sprintLocalDateTime)
                            Text(text = formattedSprintDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.time?.let{
                            val raceLocalDateTime = OffsetDateTime.parse("${race.date}T${race.time}")
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                            val formattedRaceDateTime = dateTimeFormatter.format(raceLocalDateTime)
                            Text(text = formattedRaceDateTime.substring(5, 10), fontSize = 14.sp, fontFamily = myCustomFont)
                        }

                    }

                    Column(
                        modifier = Modifier
                            .weight(1f).fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Óraidőpont
                        race.FirstPractice?.let{
                            val firstPracticeLocalDateTime =
                                OffsetDateTime.parse("${race.FirstPractice.date}T${race.FirstPractice.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedFirstPracticeDateTime =
                                dateTimeFormatter.format(firstPracticeLocalDateTime)
                            Text(text = formattedFirstPracticeDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.SecondPractice?.let {
                            val secondPracticeLocalDateTime =
                                OffsetDateTime.parse("${race.SecondPractice.date}T${race.SecondPractice.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedSecondPracticeDateTime =
                                dateTimeFormatter.format(secondPracticeLocalDateTime)
                            Text(text = formattedSecondPracticeDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }


                        race.ThirdPractice?.let {
                            val thirdPracticeLocalDateTime =
                                OffsetDateTime.parse("${it.date}T${it.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedThirdPracticeDateTime =
                                dateTimeFormatter.format(thirdPracticeLocalDateTime)
                            Text(text = formattedThirdPracticeDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.Qualifying?.let {
                            val qualifyingLocalDateTime =
                                OffsetDateTime.parse("${race.Qualifying.date}T${race.Qualifying.time}")
                                    .atZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            val formattedQualifyingDateTime =
                                dateTimeFormatter.format(qualifyingLocalDateTime)
                            Text(text = formattedQualifyingDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }


                        race.Sprint?.let {
                            val sprintLocalDateTime = OffsetDateTime.parse("${it.date}T${it.time}")
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                            val formattedSprintDateTime = dateTimeFormatter.format(sprintLocalDateTime)
                            Text(text = formattedSprintDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }
                        race.time?.let{
                            val raceLocalDateTime = OffsetDateTime.parse("${race.date}T${race.time}")
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()
                            val formattedRaceDateTime = dateTimeFormatter.format(raceLocalDateTime)
                            Text(text = formattedRaceDateTime.substring(11), fontSize = 14.sp, fontFamily = myCustomFont)
                        }

                    }
                }
            }
        }
    }
}
private val notificationChannelId = "Race_Start_Notification_Channel"

fun sendRaceStartNotification(context: Context, raceName: String) {
    val builder = NotificationCompat.Builder(context, notificationChannelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Race Start Reminder")
        .setContentText("$raceName is starting in 10 minutes!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(1, builder.build())
    }
}
