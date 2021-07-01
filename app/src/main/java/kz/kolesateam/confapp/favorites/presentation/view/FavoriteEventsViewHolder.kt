package kz.kolesateam.confapp.favorites.presentation.view

import android.view.View
import android.widget.*
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.events.presentation.view.AllEventsClickListener
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.favorites.domain.FavoriteActionEvent
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.common.extensions.getEventFormattedDateTime
import java.util.*

private const val FORMAT_STRING_FOR_DATE_AND_PLACE = "%s - %s • %s"
private const val DATE_TIME_FORMAT = "HH:mm"

class FavoriteEventsViewHolder(
    view: View,
    private val favoriteEventsViewHolderListener: AllEventsClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : BaseViewHolder<EventData>(view) {
    private val favoriteObserver: Observer = object : Observer {
        override fun update(o: Observable?, favoriteActionEventObject: Any?) {
            val favoriteEventAction = (favoriteActionEventObject as? FavoriteActionEvent) ?: return

            if(eventData.id == favoriteEventAction.eventId) {
                toFavoritesImageButton.setImageResource(
                    getFavoriteImageResource(favoriteEventAction.isFavorite)
                )
            }
        }
    }
    private val favoriteEvent: View =
        view.findViewById(R.id.activity_all_events_event_card)

    private val dateAndPlaceTextView: TextView =
        favoriteEvent.findViewById(R.id.events_time_and_place_text_view)
    private val speakerFullNameTextView: TextView =
        favoriteEvent.findViewById(R.id.events_speaker_name_text_view)
    private val speakerJobTextView: TextView =
        favoriteEvent.findViewById(R.id.events_speaker_job_place_text_view)
    private val eventTitleTextView: TextView =
        favoriteEvent.findViewById(R.id.events_title_text_view)
    private val toFavoritesImageButton: ImageView =
        favoriteEvent.findViewById(R.id.to_favorite_short_image_button)
    private lateinit var eventData: EventData

    init {
        favoriteEvent.findViewById<TextView>(
            R.id.events_state_flag_text_view
        ).visibility = View.INVISIBLE
    }

    override fun onBind(eventData: EventData) {
        this.eventData = eventData
        fillEvent(eventData)
        setOnClickListeners(eventData)
        favoriteEventActionObservable.subscribe(favoriteObserver)
    }

    fun onViewRecycled() {
        favoriteEventActionObservable.unsubscribe(favoriteObserver)
    }

    private fun fillEvent(
        eventData: EventData,
    ) {
        val favoriteImageResource = getFavoriteImageResource(eventData.isFavorite)

        dateAndPlaceTextView.text = formatStringForDateAndPlace(eventData)
        speakerFullNameTextView.text = eventData.speaker.fullName
        speakerJobTextView.text = eventData.speaker.job
        eventTitleTextView.text = eventData.title
        toFavoritesImageButton.setImageResource(favoriteImageResource)
    }

    private fun formatStringForDateAndPlace(event: EventData): String {
        val startTime = event.startTime.getEventFormattedDateTime(DATE_TIME_FORMAT)
        val endTime = event.endTime.getEventFormattedDateTime(DATE_TIME_FORMAT)
        return String.format(
            FORMAT_STRING_FOR_DATE_AND_PLACE,
            startTime,
            endTime,
            event.place
        )
    }

    private fun setOnClickListeners(event: EventData) {

        favoriteEvent.setOnClickListener {
            favoriteEventsViewHolderListener.onEventClick(
                event
            )
        }

        toFavoritesImageButton.setOnClickListener {
            event.isFavorite = !event.isFavorite
            val favoriteImageResource = getFavoriteImageResource(event.isFavorite)
            toFavoritesImageButton.setImageResource(favoriteImageResource)
            favoriteEventsViewHolderListener.onFavoritesClicked(event)
        }

    }

    private fun getFavoriteImageResource(
        isFavorite: Boolean
    ): Int = when (isFavorite) {
        true -> R.drawable.ic_favorite_fill
        else -> R.drawable.ic_favorite_border
    }
}