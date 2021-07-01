package kz.kolesateam.confapp.upcoming.data.datasource

import kz.kolesateam.confapp.common.models.BranchApiData
import retrofit2.Call
import retrofit2.http.GET

interface UpcomingEventsDataSource {
    @GET("/upcoming_events")
    fun getUpcomingEventsAuto(): Call<List<BranchApiData>>
}