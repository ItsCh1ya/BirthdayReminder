package ru.chiya.birthdatereminder.common

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.chiya.birthdatereminder.R

class BirthdayNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val notification = buildNotification(context, intent)
            showNotification(context, notification)
        }
    }

    private fun buildNotification(context: Context, intent: Intent?): NotificationCompat.Builder {
        val channelId = "birthday_channel" // Define your notification channel
        val name = intent?.getStringExtra("name")
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Birthday Reminder")
            .setContentText("It's ${name}'s birthday today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    private fun showNotification(context: Context, notification: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissionIntent = Intent().apply {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
                context.startActivity(permissionIntent)
                return
            }
            notify(notificationId, notification.build())
        }
    }

    companion object {
        private const val notificationId = 1001 // Unique notification ID
    }
}