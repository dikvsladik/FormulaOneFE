package hu.bme.aut.android.formulaonefe

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.formulaonefe.data.driverinformation.DriverInformation
import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceTable
import hu.bme.aut.android.formulaonefe.data.result.Result
import hu.bme.aut.android.formulaonefe.data.standings.StandingsLists
import hu.bme.aut.android.formulaonefe.data.worldchampoins.Champions
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.notification.NotificationHelper
import hu.bme.aut.android.formulaonefe.ui.common.*
import hu.bme.aut.android.formulaonefe.ui.tools.DriverDetailsScreen
import hu.bme.aut.android.formulaonefe.ui.tools.SettingsScreen
import kotlinx.coroutines.launch
import java.time.LocalDate


class MainActivity : ComponentActivity() {

    private val notificationAccessRequestCode = 100
    private val channelId = "my_channel_id"
    private val notificationId = 1

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        val repository = FormulaRepository(ApiClient.api)

        setContent {
            val apiAvailable = remember { mutableStateOf(true) }
            val standingsLists = remember { mutableStateOf(emptyList<StandingsLists>()) }
            val scheduleLists = remember { mutableStateOf<RaceTable?>(null) }
            val resultLists = remember { mutableStateListOf<hu.bme.aut.android.formulaonefe.data.result.Result>() }
            val latestResultRound = remember { mutableStateOf("0") }
            val driversList = remember { mutableStateOf<DriverInformation?>(null) }
            val championsList = remember { mutableStateOf<Champions?>(null) }

            fetchData(repository, apiAvailable, standingsLists, scheduleLists, latestResultRound, resultLists, driversList, championsList)

            MaterialTheme {
                Surface(color = Color(51, 51, 51)) {
                    if (apiAvailable.value) {
                        setupNavigation(standingsLists, scheduleLists, resultLists, driversList, championsList)
                    } else {
                        showApiUnavailableMessage()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchData(
        repository: FormulaRepository,
        apiAvailable: MutableState<Boolean>,
        standingsLists: MutableState<List<StandingsLists>>,
        scheduleLists: MutableState<RaceTable?>,
        latestResultRound: MutableState<String>,
        resultLists: MutableList<hu.bme.aut.android.formulaonefe.data.result.Result>,
        driversList: MutableState<DriverInformation?>,
        championsList: MutableState<Champions?>
    ) {
        lifecycleScope.launch {
            try {
                standingsLists.value = repository.getFormula(LocalDate.now().year.toString())!!.MRData.StandingsTable.StandingsLists
            } catch (e: Exception) {
                apiAvailable.value = false
            }
        }
        lifecycleScope.launch {
            try {
                scheduleLists.value = repository.getSchedule(LocalDate.now().year.toString())!!.MRData.RaceTable
            } catch (e: Exception) {
                apiAvailable.value = false
            }
        }
        lifecycleScope.launch {
            try {
                latestResultRound.value = repository.getLatest()!!.MRData.RaceTable.round
            } catch (e: Exception) {
                apiAvailable.value = false
            }
        }
        var latestResultRoundInt = latestResultRound.value.toIntOrNull() ?: 0
        lifecycleScope.launch {
            for (race in 1..latestResultRoundInt) {
                try {
                    val result = repository.getResult(year = LocalDate.now().year.toString(), race = race.toString())
                    result?.MRData?.let { resultList ->
                        resultLists.add(Result(resultList))
                    }
                } catch (e: Exception) {
                    apiAvailable.value = false
                }
            }
        }
        lifecycleScope.launch {
            try {
                driversList.value = repository.getDrivers(LocalDate.now().year.toString())
            } catch (e: Exception) {
                apiAvailable.value = false
            }
        }
        lifecycleScope.launch {
            try {
                championsList.value = repository.getChampions()
            } catch (e: Exception) {
                apiAvailable.value = false
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun setupNavigation(
        standingsLists: MutableState<List<StandingsLists>>,
        scheduleLists: MutableState<RaceTable?>,
        resultLists: MutableList<hu.bme.aut.android.formulaonefe.data.result.Result>,
        driversList: MutableState<DriverInformation?>,
        championsList: MutableState<Champions?>


    ) {

        val navController = rememberNavController()
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.DriverStandings.route) { DriverStandingsScreen() }
            composable(Screen.RaceSchedule.route) { scheduleLists.value?.let { it1 ->
                RaceScheduleScreen(
                    it1, lifecycleScope
                )
            } }
            composable(Screen.RaceResult.route) { RaceResultScreen(resultLists,lifecycleScope) }
            composable(Screen.DriverInformation.route) {
                DriverInformationScreen(driversList.value,lifecycleScope) { driver ->
                    navController.navigate("driverDetails/${driver.driverId}")
                }
            }
            composable("driverDetails/{driverId}") { backStackEntry ->
                val driverId = backStackEntry.arguments?.getString("driverId")
                val driver = driversList.value?.MRData?.DriverTable?.Drivers?.find { it.driverId == driverId }
                if (driver != null) {
                    DriverDetailsScreen(driver = driver) {
                        // Handle Wikipedia button click here
                    }
                }
            }

            composable(Screen.WorldChampions.route) { WorldChampionsScreen(championsList.value,lifecycleScope) }

            composable("settings") { SettingsScreen(showNotification = { requestNotificationAccess() }) }
        }
    }
    @Composable
    private fun showApiUnavailableMessage() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "The database is currently unavailable, please try again later.",
                color = Color.White,
                fontSize = 18.sp, modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center, fontFamily = myCustomFont
            )
        }
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

    private fun requestNotificationAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivityForResult(intent, notificationAccessRequestCode)
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
    }

    private fun showNotification() {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.showNotification()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == notificationAccessRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    showNotification()
                } else {
                    Toast.makeText(this, "Notification access not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

