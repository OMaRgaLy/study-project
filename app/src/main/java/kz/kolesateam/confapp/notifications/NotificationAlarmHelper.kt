package kz.kolesateam.confapp.notifications

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.common.models.EventData

const val NOTIFICATION_CONTENT_KEY = "notification_title"
const val NOTIFICATION_EVENT_ID_KEY = "event_id"

class NotificationAlarmHelper(
    private val application: Application,
) {
    private var pendingIntent: PendingIntent? = null
    private val alarmManager: AlarmManager? = application.getSystemService(
        Context.ALARM_SERVICE
    ) as? AlarmManager

    fun createNotificationAlarm(eventData: EventData) {
        if(!eventData.isCompleted) {
            pendingIntent =
                Intent(application, NotificationAlarmBroadcastReceiver::class.java).apply {
                    putExtra(NOTIFICATION_CONTENT_KEY, eventData.title)
                    putExtra(NOTIFICATION_EVENT_ID_KEY, eventData.id)
                }.let {
                    PendingIntent.getBroadcast(application,
                        eventData.id,
                        it,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                }
            val fiveMinutesInSeconds = 300
            val fiveMinutesBeforeTheStartOfTheEvent: Long =
                (eventData.startTime.toEpochSecond() - fiveMinutesInSeconds) * 1000

            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                fiveMinutesBeforeTheStartOfTheEvent,
                pendingIntent
            )
        }
    }

    fun cancelNotificationAlarm(eventData: EventData) {
        pendingIntent ?: return
        val intent = Intent(application, NotificationAlarmBroadcastReceiver::class.java)
        val pendingIn = PendingIntent.getBroadcast(application,
            eventData.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager?.cancel(pendingIn)
    }
}