package ru.chiya.birthdatereminder.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import java.util.*

class BirthdayNotificationManager(private val context: Context) {

    fun scheduleBirthdayNotification(birthday: BirthdayEntity) {
        val nextBirthdayTime = calculateNextBirthdayTime(birthday.birthday)
        val notificationIntent = createNotificationIntent(birthday.name)
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

    fun cancelBirthdayNotification(birthdayId: Int) {
        val notificationIntent = createNotificationIntent("") // Empty name
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            birthdayId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun updateBirthdayNotification(birthday: BirthdayEntity) {
        cancelBirthdayNotification(birthday.id)
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

    private fun createNotificationIntent(name: String): Intent {
        val intent = Intent(context, BirthdayNotificationReceiver::class.java)
        intent.putExtra(EXTRA_BIRTHDAY_NAME, name)
        return intent
    }

    companion object {
        const val EXTRA_BIRTHDAY_NAME = "extra_birthday_name"
    }
}
