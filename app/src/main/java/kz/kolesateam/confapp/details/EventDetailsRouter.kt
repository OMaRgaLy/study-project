package kz.kolesateam.confapp.details

import android.content.Context
import android.content.Intent

const val EVENT_ID = "event_id"

class EventDetailsRouter {

    private fun createIntent(
        context: Context
    ): Intent = Intent(context, EventDetailsActivity::class.java)

    fun createIntentForEventDetails(
        context: Context,
        eventId: Int,
    ): Intent = createIntent(context).apply {
        putExtra(EVENT_ID, eventId)
    }
}