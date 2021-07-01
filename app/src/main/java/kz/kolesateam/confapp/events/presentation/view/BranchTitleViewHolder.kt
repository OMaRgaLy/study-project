package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.*
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.events.data.models.AllEventsListItem

class BranchTitleViewHolder(view: View) : BaseViewHolder<AllEventsListItem>(view) {

    private val branchNameTextView: TextView =
        view.findViewById(R.id.events_branch_title_text_view)

    override fun onBind(data: AllEventsListItem) {
        branchNameTextView.text = (data as? AllEventsListItem.BranchTitleItem)?.branchTitle
    }
}