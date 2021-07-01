package kz.kolesateam.confapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val DEFAULT_EVENT_ID = 0

class NotificationAlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val content = intent?.getStringExtra(NOTIFICATION_CONTENT_KEY).orEmpty()
        val eventId =
            intent?.getIntExtra(NOTIFICATION_EVENT_ID_KEY, DEFAULT_EVENT_ID) ?: DEFAULT_EVENT_ID

        NotificationHelper.sendNotification(
            title = "Не пропустите следующий доклад",
            content = content,
            eventId = eventId,
        )
    }
}