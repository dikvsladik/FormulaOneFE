package hu.bme.aut.android.formulaonefe.notification

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import hu.bme.aut.android.formulaonefe.ui.common.sendRaceStartNotification
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class RaceStartReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val raceName = inputData.getString("raceName") ?: return Result.failure()
        sendRaceStartNotification(applicationContext, raceName)
        return Result.success()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleRaceStartReminder(context: Context, raceName: String, raceDateTime: LocalDateTime) {
    val raceDateTimeUtc = ZonedDateTime.of(raceDateTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
    val reminderDateTime = raceDateTimeUtc.minusMinutes(10)
    val duration = Duration.between(LocalDateTime.now(ZoneId.of("UTC")), reminderDateTime)

    if (duration.isNegative) {
        return
    }

    val workRequest = OneTimeWorkRequest.Builder(RaceStartReminderWorker::class.java)
        .setInputData(workDataOf("raceName" to raceName))
        .setInitialDelay(duration.toMillis(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
