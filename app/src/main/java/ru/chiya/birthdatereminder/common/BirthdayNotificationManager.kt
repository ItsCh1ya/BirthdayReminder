package ru.chiya.birthdatereminder.common

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import java.util.*

class BirthdayNotificationManager(private val context: Context) {

    fun scheduleBirthdayNotification(birthday: BirthdayEntity) {
        createNotificationChannel()
        val nextBirthdayTime = calculateNextBirthdayTime(birthday.birthday)
        val notificationIntent = createNotificationIntent(birthday)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            birthday.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthdayTime.time,
            pendingIntent
        )
    }

    fun cancelBirthdayNotification(birthday: BirthdayEntity) {
        val notificationIntent = createNotificationIntent(birthday) // Empty name
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            birthday.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun updateBirthdayNotification(birthday: BirthdayEntity) {
        cancelBirthdayNotification(birthday)
        scheduleBirthdayNotification(birthday)
    }

    private fun calculateNextBirthdayTime(birthday: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = birthday

        val currentYear = calendar.get(Calendar.YEAR)
        val today = Calendar.getInstance()

        // If the birthday has already occurred this year, schedule for next year
        if (today.time.after(birthday)) {
            calendar.set(Calendar.YEAR, currentYear + 1)
        } else {
            calendar.set(Calendar.YEAR, currentYear)
        }

        return calendar.time
    }

    private fun createNotificationIntent(birthday: BirthdayEntity): Intent {
        val intent = Intent(context, BirthdayNotificationReceiver::class.java)
        intent.putExtra("name", birthday.name)
        intent.putExtra("id", birthday.id)
        intent.putExtra("description", birthday.description)
        intent.putExtra("category", birthday.category)
        return intent
    }
    private fun createNotificationChannel() {
        val channelId = "birthday_channel"
        val channelName = "Birthday Notifications"
        val channelDescription = "Notifications for upcoming birthdays"

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
