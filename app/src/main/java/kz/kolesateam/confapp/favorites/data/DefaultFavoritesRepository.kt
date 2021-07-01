package kz.kolesateam.confapp.favorites.data

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.MapType
import kz.kolesateam.confapp.common.models.EventApiData
import kz.kolesateam.confapp.common.mappers.EventApiDataMapper
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import java.io.FileInputStream
import java.io.FileOutputStream
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable

private const val FAVORITE_EVENTS_FILE_NAME = "favorite_events.json"

class DefaultFavoritesRepository(
    private val context: Context,
    private val objectMapper: ObjectMapper,
    private val eventApiDataMapper: EventApiDataMapper,
    private val favoriteEventActionObservable: FavoriteEventActionObservable,
) : FavoritesRepository {
    private var favoriteEvents: MutableMap<Int, EventData> = mutableMapOf()

    init {
        val favoriteEventsFromFile = getFavoriteEventsFromFile()
        favoriteEvents.putAll(favoriteEventsFromFile)
    }

    override fun saveFavoriteEvent(eventData: EventData) {
        favoriteEvents[eventData.id] = eventData
        saveFavoriteEventsToFile()
        favoriteEventActionObservable.notifyChanged(
            eventId = eventData.id,
            isFavorite = true
        )
    }

    override fun removeFavoriteEvent(eventId: Int) {
        favoriteEvents.remove(eventId)
        saveFavoriteEventsToFile()
        favoriteEventActionObservable.notifyChanged(
            eventId = eventId,
            isFavorite = false
        )
    }

    override fun getAllFavoriteEvents(): List<EventData> {
        return favoriteEvents.values.toList()
    }

    override fun isFavorite(id: Int): Boolean = favoriteEvents.containsKey(id)

    override fun getFavoriteEvent(id: Int): EventData {
        return favoriteEvents.values.toList()[id]
    }

    private fun saveFavoriteEventsToFile() {
        val saveFavoritesMapToFile: MutableMap<Int, EventApiData> = mutableMapOf()

        favoriteEvents.values.forEach { eventData ->
            saveFavoritesMapToFile[eventData.id] = eventApiDataMapper.map(eventData)
        }

        val favoriteEventsJsonString: String =
            objectMapper.writeValueAsString(saveFavoritesMapToFile)
        val fileOutputStream: FileOutputStream = context.openFileOutput(
            FAVORITE_EVENTS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        fileOutputStream.write(favoriteEventsJsonString.toByteArray())
        fileOutputStream.close()
    }

    private fun getFavoriteEventsFromFile(): Map<Int, EventData> {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(FAVORITE_EVENTS_FILE_NAME)
        } catch (exception: Exception) {
            fileInputStream?.close()
            return emptyMap()
        }
        val favoriteEventsJsonString: String =
            fileInputStream?.bufferedReader()?.readLines()?.joinToString().orEmpty()

        if (favoriteEventsJsonString.isBlank()) return emptyMap()

        val mapType: MapType = objectMapper.typeFactory.constructMapType(
            Map::class.java,
            Int::class.java,
            EventApiData::class.java
        )

        val favoritesFromFile: MutableMap<Int, EventData> = mutableMapOf()
        val favoritesEventApiDataFromFile: Map<Int, EventApiData>? =
            (objectMapper.readValue(favoriteEventsJsonString, mapType)) as? Map<Int, EventApiData>

        favoritesEventApiDataFromFile?.values?.forEach { eventApiData ->
            eventApiData.id?.let {
                favoritesFromFile[eventApiData.id] = eventApiDataMapper.map(eventApiData)
            } ?: return@forEach
        }

        return favoritesFromFile
    }
}