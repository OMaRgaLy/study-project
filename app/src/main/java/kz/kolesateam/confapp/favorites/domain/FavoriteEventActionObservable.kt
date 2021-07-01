package kz.kolesateam.confapp.favorites.domain

import java.util.*

class FavoriteEventActionObservable : Observable() {
    fun subscribe(favoritesObserver: Observer) {
        addObserver(favoritesObserver)
    }

    fun unsubscribe(favoritesObserver: Observer) {
        deleteObserver(favoritesObserver)
    }

    fun notifyChanged(eventId: Int, isFavorite: Boolean) {
        notify(
            FavoriteActionEvent(
                eventId = eventId,
                isFavorite = isFavorite,
            )
        )
    }

    private fun notify(favoriteActionEvent: FavoriteActionEvent) {
        setChanged()
        notifyObservers(favoriteActionEvent)
    }
}