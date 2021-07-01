package kz.kolesateam.confapp.upcoming.domain

import androidx.annotation.WorkerThread
import kz.kolesateam.confapp.common.models.BranchData
import kz.kolesateam.confapp.common.utils.ResponseData

interface UpcomingEventsRepository {
    @WorkerThread
    fun getUpcomingEvents() : ResponseData<List<BranchData>, Exception>
}