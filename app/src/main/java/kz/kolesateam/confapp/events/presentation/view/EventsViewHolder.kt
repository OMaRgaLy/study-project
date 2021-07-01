package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.*
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.data.models.AllEventsListItem
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.common.extensions.getEventFormattedDateTime
import java.util.*
import kz.kolesateam.confapp.favorites.domain.FavoriteActionEvent

private const val FORMAT_STRING_FOR_DATE_AND_PLACE = "%s - %s • %s"
private const val DATE_TIME_FORMAT = "HH:mm"

class EventsViewHolder(
    view: View,
    private val allEventsViewHolderListener: AllEventsClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable,
) : BaseViewHolder<AllEventsListItem>(view) {
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

    private val branchEvent: View =
        view.findViewById(R.id.activity_all_events_event_card)
    private val stateEventTextView: TextView =
        branchEvent.findViewById(R.id.events_state_flag_text_view)

    private val eventPaddingLeft = branchEvent.paddingLeft
    private val eventPaddingTop = branchEvent.paddingTop
    private val eventPaddingRight = branchEvent.paddingRight
    private val eventPaddingBottom = branchEvent.paddingBottom


    private val dateAndPlaceTextView: TextView =
        branchEvent.findViewById(R.id.events_time_and_place_text_view)
    private val speakerFullNameTextView: TextView =
        branchEvent.findViewById(R.id.events_speaker_name_text_view)
    private val speakerJobTextView: TextView =
        branchEvent.findViewById(R.id.events_speaker_job_place_text_view)
    private val eventTitleTextView: TextView =
        branchEvent.findViewById(R.id.events_title_text_view)
    private val toFavoritesImageButton: ImageView =
        branchEvent.findViewById(R.id.to_favorite_short_image_button)

    private lateinit var eventData: EventData

    init {
        branchEvent.findViewById<TextView>(
            R.id.events_state_flag_text_view
        ).visibility = View.INVISIBLE
    }

    fun onViewRecycled() {
        favoriteEventActionObservable.unsubscribe(favoriteObserver)
    }

    override fun onBind(data: AllEventsListItem) {
        eventData = (data as? AllEventsListItem.EventListItem)?.data ?: return
        fillEvent(eventData)
        setOnClickListeners(eventData)
        favoriteEventActionObservable.subscribe(favoriteObserver)
    }

    private fun fillEvent(
        eventData: EventData,
    ) {
        dateAndPlaceTextView.text = formatStringForDateAndPlace(eventData)
        speakerFullNameTextView.text = eventData.speaker.fullName
        speakerJobTextView.text = eventData.speaker.job
        eventTitleTextView.text = eventData.title
        setBackgroundEvent(eventData.isCompleted)
        toFavoritesImageButton.setImageResource(getFavoriteImageResource(eventData.isFavorite))
    }

    private fun setBackgroundEvent(isEndEvent: Boolean) {
        if (isEndEvent) {
            stateEventTextView.visibility = View.VISIBLE
            stateEventTextView.setBackgroundResource(R.drawable.bg_event_flag_end_text_view)
            stateEventTextView.text =
                stateEventTextView.context.getString(R.string.ended_event_text)

            branchEvent.setBackgroundResource(R.drawable.bg_unfocused_events_card_info)
            branchEvent.setPadding(
                eventPaddingLeft,
                eventPaddingTop,
                eventPaddingRight,
                eventPaddingBottom
            )
        } else {
            stateEventTextView.visibility = View.INVISIBLE
            branchEvent.setBackgroundResource(R.drawable.bg_events_card_info)
            branchEvent.setPadding(
                eventPaddingLeft,
                eventPaddingTop,
                eventPaddingRight,
                eventPaddingBottom
            )
        }
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

        branchEvent.setOnClickListener {
            allEventsViewHolderListener.onEventClick(
                event
            )
        }

        toFavoritesImageButton.setOnClickListener {
            event.isFavorite = !event.isFavorite
            val favoriteImageResource = getFavoriteImageResource(event.isFavorite)
            toFavoritesImageButton.setImageResource(favoriteImageResource)
            allEventsViewHolderListener.onFavoritesClicked(event)
        }

    }
    private fun getFavoriteImageResource(
        isFavorite: Boolean
    ): Int = when (isFavorite) {
        true -> R.drawable.ic_favorite_fill
        else -> R.drawable.ic_favorite_border
    }
}