package hu.bme.aut.android.formulaonefe.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import hu.bme.aut.android.formulaonefe.data.raceschedule.Race
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification()
    }
}


fun scheduleNotification(context: Context, time: String,date:String) {
    val notificationIntent = Intent(context, NotificationReceiver::class.java)
    val requestCode = 0
    val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, pendingIntentFlags)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val dateTimeInMillis = convertDateTimeToMillis(date, time)
    val triggerTime = dateTimeInMillis - TimeUnit.MINUTES.toMillis(30) // 30 minutes before the specified time
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}
fun convertDateTimeToMillis(dateString: String, timeString: String): Long {
    val dateTimeString = "$dateString $timeString"
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = inputFormat.parse(dateTimeString)

    return date?.time ?: 0L
}


