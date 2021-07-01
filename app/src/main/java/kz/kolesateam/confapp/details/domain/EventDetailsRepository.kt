package kz.kolesateam.confapp.details.domain

import androidx.annotation.WorkerThread
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.common.utils.ResponseData

interface EventDetailsRepository {
    @WorkerThread
    fun getEventDetails(eventId: Int): ResponseData<EventData, Exception>
}