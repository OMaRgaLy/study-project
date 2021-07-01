package kz.kolesateam.confapp.upcoming.presentation.view

import android.view.View
import android.widget.TextView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.upcoming.presentation.models.UpcomingEventsListItem

class HeaderViewHolder(view: View) : BaseViewHolder<UpcomingEventsListItem>(view) {
    private val helloUserTextView: TextView =
        view.findViewById(R.id.header_user_name_text_view)

    override fun onBind(data: UpcomingEventsListItem) {
        val userName: String = (data as? UpcomingEventsListItem.HeaderItem)?.userName ?: return

        helloUserTextView.text = helloUserTextView.resources.getString(
            R.string.upcoming_events_activity_greetings_user_name_fmt,
            userName
        )
    }
}