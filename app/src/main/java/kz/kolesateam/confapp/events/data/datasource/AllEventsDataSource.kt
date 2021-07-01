package kz.kolesateam.confapp.events.data.datasource

import kz.kolesateam.confapp.common.models.EventApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AllEventsDataSource {

    @GET("/branch_events/{branch_id}")
    fun getAllEventsAuto(@Path("branch_id") branchId: Int): Call<List<EventApiData>>
}