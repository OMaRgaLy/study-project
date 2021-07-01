package kz.kolesateam.confapp.common.mappers

import android.net.ParseException
import kz.kolesateam.confapp.common.models.EventApiData
import kz.kolesateam.confapp.common.models.EventData
import org.threeten.bp.ZonedDateTime

private const val DEFAULT_EVENT_TITLE = ""
private const val DEFAULT_EVENT_DESCRIPTION = ""
private const val DEFAULT_EVENT_ID = 0
private const val DEFAULT_START_TIME_TEXT = ""
private const val DEFAULT_END_TIME_TEXT = ""
private const val DEFAULT_PLACE_TEXT = ""

class EventApiDataMapper {

    fun mapToListEventData(eventApiData: List<EventApiData>?): List<EventData> {
        eventApiData ?: return emptyList()

        return eventApiData.map {
            val startTime = getTimeByFormat(it.startTime ?: DEFAULT_START_TIME_TEXT)
            val endTime = getTimeByFormat(it.endTime ?: DEFAULT_END_TIME_TEXT)
            EventData(
                id = it.id ?: DEFAULT_EVENT_ID,
                startTime = startTime,
                endTime = endTime,
                title = it.title ?: DEFAULT_EVENT_TITLE,
                description = it.description ?: DEFAULT_EVENT_DESCRIPTION,
                place = it.place ?: DEFAULT_PLACE_TEXT,
                speaker = SpeakerApiDataMapper().map(it.speaker)
            )
        }
    }

    fun mapToListEventApiData(eventData: List<EventData>?): List<EventApiData> {
        eventData ?: return emptyList()

        return eventData.map {
            val startTime = it.startTime.toString()
            val endTime = it.endTime.toString()
            EventApiData(
                id = it.id ?: DEFAULT_EVENT_ID,
                startTime = startTime,
                endTime = endTime,
                title = it.title ?: DEFAULT_EVENT_TITLE,
                description = it.description ?: DEFAULT_EVENT_DESCRIPTION,
                place = it.place ?: DEFAULT_PLACE_TEXT,
                speaker = SpeakerApiDataMapper().map(it.speaker)
            )
        }
    }

    fun map(eventApiData: EventApiData): EventData {
        val startTime = getTimeByFormat(eventApiData.startTime ?: DEFAULT_START_TIME_TEXT)
        val endTime = getTimeByFormat(eventApiData.endTime ?: DEFAULT_END_TIME_TEXT)
        return EventData(
            id = eventApiData.id ?: DEFAULT_EVENT_ID,
            startTime = startTime,
            endTime = endTime,
            title = eventApiData.title ?: DEFAULT_EVENT_TITLE,
            description = eventApiData.description ?: DEFAULT_EVENT_DESCRIPTION,
            place = eventApiData.place ?: DEFAULT_PLACE_TEXT,
            speaker = SpeakerApiDataMapper().map(eventApiData.speaker)
        )
    }

    fun map(eventData: EventData?): EventApiData {
        val startTime = eventData?.startTime.toString()
        val endTime = eventData?.endTime.toString()
        return EventApiData(
            id = eventData?.id ?: DEFAULT_EVENT_ID,
            startTime = startTime,
            endTime = endTime,
            title = eventData?.title ?: DEFAULT_EVENT_TITLE,
            description = eventData?.description ?: DEFAULT_EVENT_DESCRIPTION,
            place = eventData?.place ?: DEFAULT_PLACE_TEXT,
            speaker = SpeakerApiDataMapper()?.map(eventData?.speaker)
        )
    }

    private fun getTimeByFormat(
        time: String,
    ): ZonedDateTime = try {
        ZonedDateTime.parse(time) ?: ZonedDateTime.now()
    } catch (e: ParseException) {
        ZonedDateTime.now()
    }
}