package kz.kolesateam.confapp.details

import kz.kolesateam.confapp.details.domain.EventDetailsRepository
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.common.utils.ProgressState
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.common.utils.ResponseData
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class EventDetailsViewModel(
    private val eventDetailsRepository: EventDetailsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper,
) : ViewModel() {
    private val progressBarLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val eventDetailsLiveData: MutableLiveData<EventData> =
        MutableLiveData()
    private val errorLiveData: MutableLiveData<Exception> = MutableLiveData()

    fun getEventDetailsLiveData(): LiveData<EventData> = eventDetailsLiveData
    fun getErrorLiveData(): LiveData<Exception> = errorLiveData
    fun getProgressBarLiveData(): LiveData<ProgressState> = progressBarLiveData

    fun onStart(eventId: Int) {
        getEventDetails(eventId)
    }

    private fun getEventDetails(eventId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            progressBarLiveData.value = ProgressState.Loading
            val eventDetailsResponse: ResponseData<EventData, Exception> =
                withContext(Dispatchers.IO) {
                    eventDetailsRepository.getEventDetails(eventId)
                }
            when (eventDetailsResponse) {
                is ResponseData.Success -> {
                    eventDetailsResponse.result.isFavorite = favoritesRepository.isFavorite(eventId)
                    eventDetailsResponse.result.isCompleted = isCompleted(eventDetailsResponse.result)
                    eventDetailsLiveData.value = eventDetailsResponse.result
                }
                is ResponseData.Error -> {
                    errorLiveData.value = eventDetailsResponse.error
                }
            }
            progressBarLiveData.value = ProgressState.Done
        }
    }

    fun onFavoriteClick(eventData: EventData) {
        when (eventData.isFavorite) {
            true -> {
                favoritesRepository.saveFavoriteEvent(eventData)
                notificationAlarmHelper.createNotificationAlarm(eventData)
            }

            else -> {
                favoritesRepository.removeFavoriteEvent(eventData.id)
                notificationAlarmHelper.cancelNotificationAlarm(eventData)
            }
        }
    }

    private fun isCompleted(eventData: EventData): Boolean {
        val dateNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(6))

        return dateNow.isAfter(eventData.endTime)
    }
}