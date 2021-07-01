package kz.kolesateam.confapp.favorites.presentation.view

import androidx.recyclerview.widget.DiffUtil
import kz.kolesateam.confapp.common.models.EventData

class FavoriteEventsDiffUtil (
    private val oldList: List<EventData>,
    private val newList: List<EventData>
) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}