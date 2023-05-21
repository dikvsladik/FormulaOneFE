package hu.bme.aut.android.formulaonefe.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.android.formulaonefe.MainActivity
import hu.bme.aut.android.formulaonefe.R

class NotificationHelper(private val context: Context) {
    private val channelId = "my_channel_id"
    private val notificationId = 1

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification(name:String="My Notification",time: String=" ") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentFlags)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("A Race is about to start!")
            .setContentText("A Grand Prix is about to start in 30 minutes.")
            .setSmallIcon(R.drawable.noti_iconpng).setColor(ContextCompat.getColor(context, R.color.red)).setColorized(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Use default sound, vibration, and lights
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to high
            .setContentIntent(pendingIntent) // Set the PendingIntent
            .setAutoCancel(true) // Automatically cancel the notification when clicked
            .setFullScreenIntent(pendingIntent, true) // Set the full-screen intent
            .setStyle( // Set the BigTextStyle
                NotificationCompat.BigTextStyle()
                    .bigText("A Grand Prix is about to start in 30 minutes. Get ready for lights out!")
            )
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
