package kz.kolesateam.confapp.favorites.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.events.presentation.view.AllEventsClickListener
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable

class FavoriteEventsAdapter(
    private val favoriteEventsAdapterListener: AllEventsClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable,
) : RecyclerView.Adapter<BaseViewHolder<EventData>>() {
    private var favoriteEventsList: List<EventData> = mutableListOf()
    private lateinit var diffResult: DiffUtil.DiffResult
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<EventData> {
        return FavoriteEventsViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.all_events_branch_item, parent, false),
            favoriteEventsViewHolderListener = favoriteEventsAdapterListener,
            favoriteEventActionObservable = favoriteEventActionObservable
        )

    }

    override fun onViewRecycled(holder: BaseViewHolder<EventData>) {
        super.onViewRecycled(holder)
        (holder as? FavoriteEventsViewHolder)?.onViewRecycled()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<EventData>, position: Int) {
        holder.onBind(favoriteEventsList[position])
    }

    override fun getItemCount(): Int = favoriteEventsList.size

    fun setList(favoriteEventsList: List<EventData>) {
        diffResult = DiffUtil.calculateDiff(FavoriteEventsDiffUtil(this.favoriteEventsList, favoriteEventsList))
        diffResult.dispatchUpdatesTo(this)
        this.favoriteEventsList = favoriteEventsList
    }
}