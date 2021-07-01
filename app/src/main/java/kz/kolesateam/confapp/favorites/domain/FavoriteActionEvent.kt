package kz.kolesateam.confapp.favorites.domain

data class FavoriteActionEvent(
    val eventId: Int,
    val isFavorite: Boolean,
)