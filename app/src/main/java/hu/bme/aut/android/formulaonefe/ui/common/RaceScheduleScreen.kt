package hu.bme.aut.android.formulaonefe.ui.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.time.LocalDateTime
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaceScheduleScreen(schedule: RaceTable) {
    LazyColumn {
        items(schedule.Races) { race ->
            RaceItem(race)
            Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaceItem(race: Race) {
    val context = LocalContext.current
    val raceDateTime = OffsetDateTime.parse("${race.date}T${race.time}").toLocalDateTime()

    LaunchedEffect(race.raceName, raceDateTime) {
        scheduleRaceStartReminder(context, race.raceName, raceDateTime)
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = race.raceName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "Circuit: ${race.Circuit.circuitName}", fontSize = 16.sp)
        Text(text = "First Practice: ${race.FirstPractice.date} ${race.FirstPractice.time}", fontSize = 14.sp)
        Text(text = "Second Practice: ${race.SecondPractice.date} ${race.SecondPractice.time}", fontSize = 14.sp)

        race.ThirdPractice?.let {
            Text(text = "Third Practice: ${it.date} ${it.time}", fontSize = 14.sp)
        }

        Text(text = "Qualifying: ${race.Qualifying.date} ${race.Qualifying.time}", fontSize = 14.sp)

        race.Sprint?.let {
            Text(text = "Sprint: ${it.date} ${it.time}", fontSize = 14.sp)
        }

        Text(text = "Race: ${race.date} ${race.time}", fontSize = 14.sp)
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
