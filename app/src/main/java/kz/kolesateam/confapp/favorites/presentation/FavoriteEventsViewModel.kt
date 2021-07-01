package kz.kolesateam.confapp.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper

class FavoriteEventsViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper,
): ViewModel() {
    private val allFavoriteEventsLiveData: MutableLiveData<List<EventData>> =
        MutableLiveData()

    fun onStart() {
        getAllFavoriteEvents()
    }

    fun onFavoriteClick(eventData: EventData) {
        when(eventData.isFavorite) {
            true -> {
                favoritesRepository.saveFavoriteEvent(eventData)
                notificationAlarmHelper.createNotificationAlarm(eventData)
                getAllFavoriteEvents()
            }

            else -> {
                favoritesRepository.removeFavoriteEvent(eventData.id)
                notificationAlarmHelper.cancelNotificationAlarm(eventData)
                getAllFavoriteEvents()
            }
        }
    }

    fun getAllFavoriteEventsLiveData(): LiveData<List<EventData>> = allFavoriteEventsLiveData

    private fun getAllFavoriteEvents() {
        allFavoriteEventsLiveData.value = favoritesRepository.getAllFavoriteEvents()
    }
}