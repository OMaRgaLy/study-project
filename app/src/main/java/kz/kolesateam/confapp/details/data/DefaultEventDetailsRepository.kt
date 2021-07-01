package kz.kolesateam.confapp.details.data

import kz.kolesateam.confapp.details.data.datasource.EventDetailsDataSource
import kz.kolesateam.confapp.details.domain.EventDetailsRepository
import kz.kolesateam.confapp.common.mappers.EventApiDataMapper
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.common.utils.ResponseData

class DefaultEventDetailsRepository(
    private val eventDetailsDataSource: EventDetailsDataSource
) : EventDetailsRepository {
    override fun getEventDetails(eventId: Int): ResponseData<EventData, Exception> {
        return try {
            val response = eventDetailsDataSource.getEventDetailsAuto(eventId).execute()

            if(response.isSuccessful) {
                ResponseData.Success(EventApiDataMapper().map(response.body()!!))
            } else {
                ResponseData.Error(Exception(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            ResponseData.Error(e)
        }
    }
}