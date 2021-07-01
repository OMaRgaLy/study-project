package kz.kolesateam.confapp.upcoming.presentation.models

import kz.kolesateam.confapp.common.models.BranchData

const val HEADER_TYPE: Int = 0
const val BRANCH_TYPE: Int = 1

sealed class UpcomingEventsListItem(
    val type: Int,
) {
    data class HeaderItem(
        val userName: String,
    ) : UpcomingEventsListItem(HEADER_TYPE)

    data class BranchListItem(
        val data: BranchData,
    ) : UpcomingEventsListItem(BRANCH_TYPE)
}