package kz.kolesateam.confapp.events.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.events.data.models.AllEventsListItem
import kz.kolesateam.confapp.events.domain.AllEventsRepository
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.common.utils.ProgressState
import kz.kolesateam.confapp.common.utils.ResponseData
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class AllEventsViewModel(
    private val allEventsRepository: AllEventsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper,
) : ViewModel() {
    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val allEventsLiveData: MutableLiveData<List<AllEventsListItem>> =
        MutableLiveData()
    private val errorLiveData: MutableLiveData<Exception> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData

    fun getAllEventsLiveData(): LiveData<List<AllEventsListItem>> = allEventsLiveData

    fun getErrorLiveData(): LiveData<Exception> = errorLiveData

    fun onStart(branchId: Int, branchTitle: String) {
        getAllEvents(branchId, branchTitle)
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

    private fun getAllEvents(branchId: Int, branchTitle: String) {
        viewModelScope.launch(Dispatchers.Main) {
            progressLiveData.value = ProgressState.Loading

            val allEventsResponse: ResponseData<List<EventData>, Exception> =
                withContext(Dispatchers.IO) {
                    allEventsRepository.getAllEvents(branchId)
                }

            when (allEventsResponse) {
                is ResponseData.Success -> {
                    val eventDataList = allEventsResponse.result

                    val allEventsListItem: MutableList<AllEventsListItem> =
                        mutableListOf()

                    val branchTitleListItem: AllEventsListItem =
                        AllEventsListItem.BranchTitleItem(branchTitle)


                    allEventsListItem.add(branchTitleListItem)

                    eventDataList.forEach { eventData ->
                        eventData.isCompleted = isCompleted(eventData)
                        eventData.isFavorite = favoritesRepository.isFavorite(eventData.id)
                        allEventsListItem.add(AllEventsListItem.EventListItem(eventData))
                    }

                    allEventsLiveData.value =
                        allEventsListItem
                }
                is ResponseData.Error -> errorLiveData.value = allEventsResponse.error
            }

            progressLiveData.value = ProgressState.Done
        }
    }

    private fun isCompleted(eventData: EventData): Boolean {
        val dateNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(6))

        return dateNow.isAfter(eventData.endTime)
        //пока вот так пусть постоит... вроде в настройках +6 часовой пояс
    }
}