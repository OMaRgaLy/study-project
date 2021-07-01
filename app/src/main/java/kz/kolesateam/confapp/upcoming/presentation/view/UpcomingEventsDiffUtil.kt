package kz.kolesateam.confapp.upcoming.presentation.view

import androidx.recyclerview.widget.DiffUtil
import kz.kolesateam.confapp.upcoming.presentation.models.UpcomingEventsListItem

class UpcomingEventsDiffUtil (
    private val oldList: List<UpcomingEventsListItem>,
    private val newList: List<UpcomingEventsListItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBranch = (oldList[oldItemPosition] as? UpcomingEventsListItem.BranchListItem)?.data ?: return false
        val newBranch = (newList[newItemPosition] as? UpcomingEventsListItem.BranchListItem)?.data ?: return false
        return oldBranch.id == newBranch.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBranch = (oldList[oldItemPosition] as? UpcomingEventsListItem.BranchListItem)?.data ?: return false
        val newBranch = (newList[newItemPosition] as? UpcomingEventsListItem.BranchListItem)?.data ?: return false
        return oldBranch == newBranch
    }

}