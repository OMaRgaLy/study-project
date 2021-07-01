package kz.kolesateam.confapp.favorites.domain

import kz.kolesateam.confapp.common.models.EventData

interface FavoritesRepository {
    fun saveFavoriteEvent(
        eventData: EventData
    )

    fun removeFavoriteEvent(
        eventId: Int
    )

    fun getAllFavoriteEvents(): List<EventData>

    fun isFavorite(id: Int): Boolean

    fun getFavoriteEvent(id: Int): EventData
}