package kz.kolesateam.confapp.events.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.events.data.models.AllEventsListItem
import kz.kolesateam.confapp.events.data.models.BRANCH_TITLE_TYPE
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable

class AllEventsAdapter(
    private val allEventsAdapterListener: AllEventsClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : RecyclerView.Adapter<BaseViewHolder<AllEventsListItem>>() {
    private var eventsDataList: List<AllEventsListItem> = mutableListOf()
    private lateinit var diffResult: DiffUtil.DiffResult
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<AllEventsListItem> {
        return when (viewType) {
            BRANCH_TITLE_TYPE -> BranchTitleViewHolder(
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.branch_title_layout, parent, false)
            )

            else -> EventsViewHolder(
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.all_events_branch_item, parent, false),
                allEventsViewHolderListener = allEventsAdapterListener,
                favoriteEventActionObservable = favoriteEventActionObservable
            )
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder<AllEventsListItem>) {
        super.onViewRecycled(holder)
        (holder as? EventsViewHolder)?.onViewRecycled()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AllEventsListItem>, position: Int) {
        holder.onBind(eventsDataList[position])
    }

    override fun getItemCount(): Int = eventsDataList.size

    override fun getItemViewType(position: Int): Int {
        return eventsDataList[position].type
    }

    fun setList(eventsDataList: List<AllEventsListItem>) {
        diffResult = DiffUtil.calculateDiff(AllEventsDiffUtil(this.eventsDataList, eventsDataList))
        diffResult.dispatchUpdatesTo(this)
        this.eventsDataList = eventsDataList
    }
}