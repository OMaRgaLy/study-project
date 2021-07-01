package kz.kolesateam.confapp.events.data

import kz.kolesateam.confapp.common.mappers.EventApiDataMapper
import kz.kolesateam.confapp.events.data.datasource.AllEventsDataSource
import kz.kolesateam.confapp.events.domain.AllEventsRepository
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.common.utils.ResponseData

class DefaultAllEventsRepository(
    private val allEventsDataSource: AllEventsDataSource,
    private val eventApiDataMapper: EventApiDataMapper,
) : AllEventsRepository {

    override fun getAllEvents(
        branchId: Int,
    ): ResponseData<List<EventData>, Exception> {
        return try {
            val response = allEventsDataSource.getAllEventsAuto(branchId).execute()

            if (response.isSuccessful) {
                val eventDataList: List<EventData> =
                    eventApiDataMapper.mapToListEventData(response.body()!!)

                ResponseData.Success(eventDataList)
            } else {
                ResponseData.Error(Exception(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            ResponseData.Error(e)
        }
    }
}