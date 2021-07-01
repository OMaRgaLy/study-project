package kz.kolesateam.confapp.upcoming.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.common.models.BranchData
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.common.preferences.UserNameDataSource
import kz.kolesateam.confapp.upcoming.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.upcoming.presentation.models.UpcomingEventsListItem
import kz.kolesateam.confapp.common.utils.ProgressState
import kz.kolesateam.confapp.common.utils.ResponseData
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class UpcomingEventsViewModel(
    private val upcomingEventsRepository: UpcomingEventsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper,
    private val userNameDataSource: UserNameDataSource,
) : ViewModel() {

    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val upcomingEventsLiveData: MutableLiveData<List<UpcomingEventsListItem>> =
        MutableLiveData()
    private val errorLiveData: MutableLiveData<Exception> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData

    fun getUpcomingEventsLiveData(): LiveData<List<UpcomingEventsListItem>> = upcomingEventsLiveData

    fun getErrorLiveData(): LiveData<Exception> = errorLiveData

    fun onStart() {
        getUpcomingEvents()
    }

    fun onFavoriteClick(eventData: EventData) {
        when (eventData.isFavorite) {
            true -> {
                favoritesRepository.saveFavoriteEvent(eventData)
                scheduleEvent(eventData)
            }

            else -> {
                favoritesRepository.removeFavoriteEvent(eventData.id)
                cancelNotificationEvent(eventData)

            }
        }
    }

    private fun scheduleEvent(eventData: EventData) {
        notificationAlarmHelper.createNotificationAlarm(eventData)
    }

    private fun cancelNotificationEvent(eventData: EventData) {
        notificationAlarmHelper.cancelNotificationAlarm(eventData)
    }

    private fun getUpcomingEvents() {
        viewModelScope.launch(Dispatchers.Main) {
            progressLiveData.value = ProgressState.Loading

            val upcomingEventsResponse: ResponseData<List<BranchData>, Exception> =
                withContext(Dispatchers.IO) {
                    upcomingEventsRepository.getUpcomingEvents()
                }

            when (upcomingEventsResponse) {
                is ResponseData.Success -> {
                    val userName = userNameDataSource.getUserName()
                    val upcomingEventListItem: MutableList<UpcomingEventsListItem> =
                        mutableListOf()

                    val headerListItem: UpcomingEventsListItem =
                        UpcomingEventsListItem.HeaderItem(userName)

                    upcomingEventListItem.add(headerListItem)

                    val branchDataList = upcomingEventsResponse.result

                    branchDataList.forEach {
                        it.events.forEach { eventData ->
                            eventData.isFavorite = favoritesRepository.isFavorite(eventData.id)
                            eventData.isCompleted = isCompleted(eventData)
                        }
                    }

                    for (index in branchDataList.indices) {
                        upcomingEventListItem.add(UpcomingEventsListItem.BranchListItem(
                            branchDataList[index]))
                    }

                    upcomingEventsLiveData.value =
                        upcomingEventListItem
                }
                is ResponseData.Error -> errorLiveData.value = upcomingEventsResponse.error
            }

            progressLiveData.value = ProgressState.Done
        }
    }
    private fun isCompleted(eventData: EventData): Boolean {
        val dateNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(6))

        return dateNow.isAfter(eventData.endTime)
    }
}