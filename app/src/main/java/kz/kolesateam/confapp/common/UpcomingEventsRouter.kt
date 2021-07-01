package kz.kolesateam.confapp.common

import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.upcoming.presentation.UpcomingEventsActivity

class UpcomingEventsRouter {
    fun createIntent(
        context: Context
    ) : Intent = Intent(context, UpcomingEventsActivity::class.java)
}