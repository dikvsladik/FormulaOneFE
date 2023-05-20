package hu.bme.aut.android.formulaonefe

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceTable
import hu.bme.aut.android.formulaonefe.data.result.Result
import hu.bme.aut.android.formulaonefe.data.result.ResultList
import hu.bme.aut.android.formulaonefe.data.standings.DriverStandings
import hu.bme.aut.android.formulaonefe.data.standings.MRData
import hu.bme.aut.android.formulaonefe.data.standings.StandingsLists
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.ui.common.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val repository = FormulaRepository(ApiClient.api)

        setContent {
            val standingsLists = remember { mutableStateOf(emptyList<StandingsLists>()) }
            val scheduleLists = remember { mutableStateOf<RaceTable?>(null) }
            val resultLists = remember { mutableStateListOf<hu.bme.aut.android.formulaonefe.data.result.Result>() }
            val latestResultRound= remember{ mutableStateOf("0")}
            lifecycleScope.launch {
                standingsLists.value = repository.getFormula(LocalDate.now().year.toString())!!.MRData.StandingsTable.StandingsLists
            }
            lifecycleScope.launch {
                scheduleLists.value = repository.getSchedule(LocalDate.now().year.toString())!!.MRData.RaceTable
            }
            lifecycleScope.launch {
                latestResultRound.value = repository.getLatest()!!.MRData.RaceTable.round
            }
            var latestResultRoundInt = latestResultRound.value.toIntOrNull() ?: 0
            lifecycleScope.launch {
                for (race in 1..latestResultRoundInt) {
                    val result = repository.getResult(year = LocalDate.now().year.toString(), race = race.toString())
                    result?.MRData?.let { resultList ->
                        resultLists.add(Result(resultList))
                    }
                }
            }


            MaterialTheme {
                Surface(color = Color(51,51,51)) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) { HomeScreen(navController) }
                        composable(Screen.DriverStandings.route) { DriverStandingsScreen(standingsLists.value,lifecycleScope) }
                        composable(Screen.RaceSchedule.route) { scheduleLists.value?.let { it1 ->
                            RaceScheduleScreen(
                                it1, lifecycleScope
                            )
                        } }
                        composable(Screen.RaceResult.route) { RaceResultScreen(resultLists, lifecycleScope) }
                        composable(Screen.DriverInformation.route) { DriverInformationScreen() }
                        composable(Screen.WorldChampions.route) { WorldChampionsScreen() }
                    }
                }
            }
        }
    }
    fun loadJsonFromAssets(context: Context, fileName: String): DriverStandings {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        return Json.decodeFromString(jsonString)
    }
    private val notificationChannelId = "Race_Start_Notification_Channel"
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
