package kz.kolesateam.confapp.common.listeners

import kz.kolesateam.confapp.common.models.EventData

interface FavoritesClickListener {
    fun onFavoritesClicked(eventData: EventData)
}