package kz.kolesateam.confapp.events.domain

import androidx.annotation.WorkerThread
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.common.utils.ResponseData

interface AllEventsRepository {
    @WorkerThread
    fun getAllEvents(branchId: Int) : ResponseData<List<EventData>, Exception>
}