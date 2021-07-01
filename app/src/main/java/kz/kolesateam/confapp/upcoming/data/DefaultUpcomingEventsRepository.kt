package kz.kolesateam.confapp.upcoming.data

import kz.kolesateam.confapp.upcoming.data.datasource.UpcomingEventsDataSource
import kz.kolesateam.confapp.common.mappers.BranchApiDataMapper
import kz.kolesateam.confapp.upcoming.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.common.models.BranchData
import kz.kolesateam.confapp.common.utils.ResponseData

class DefaultUpcomingEventsRepository(
    private val upcomingEventsDataSource: UpcomingEventsDataSource,
    private val branchApiDataMapper: BranchApiDataMapper,
) : UpcomingEventsRepository {

    override fun getUpcomingEvents(): ResponseData<List<BranchData>, Exception> {
        return try {
            val response = upcomingEventsDataSource.getUpcomingEventsAuto().execute()

            if (response.isSuccessful) {

                val branchDataList: List<BranchData> =
                    branchApiDataMapper.map(response.body()!!)

                ResponseData.Success(branchDataList)
            } else {
                ResponseData.Error(Exception(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            return ResponseData.Error(e)
        }
    }
}