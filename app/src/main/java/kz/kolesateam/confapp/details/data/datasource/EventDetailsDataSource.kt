package kz.kolesateam.confapp.details.data.datasource

import kz.kolesateam.confapp.common.models.EventApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailsDataSource {

    @GET("/events/{event_id}")
    fun getEventDetailsAuto(@Path("event_id") eventId: Int): Call<EventApiData>
}