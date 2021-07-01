package kz.kolesateam.confapp.common.mappers

import kz.kolesateam.confapp.common.models.BranchApiData
import kz.kolesateam.confapp.common.models.BranchData

private const val DEFAULT_BRANCH_NAME = ""
private const val DEFAULT_BRANCH_ID = 0

class BranchApiDataMapper {

    fun map(data: List<BranchApiData>?): List<BranchData> {
        data ?: return emptyList()

        return data.map {
            BranchData(
                id = it.id ?: DEFAULT_BRANCH_ID,
                title = it.title ?: DEFAULT_BRANCH_NAME,
                events = EventApiDataMapper().mapToListEventData(it.events)
            )
        }
    }
}