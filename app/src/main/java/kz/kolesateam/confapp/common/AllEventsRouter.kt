package kz.kolesateam.confapp.common

import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.common.models.BranchData
import kz.kolesateam.confapp.events.presentation.AllEventsActivity
import kz.kolesateam.confapp.upcoming.presentation.BRANCH_ID
import kz.kolesateam.confapp.upcoming.presentation.BRANCH_TITLE

class AllEventsRouter(
        private val context: Context
) {
    fun createIntent(
        branchData: BranchData
    ): Intent {
        val intent = Intent(context, AllEventsActivity::class.java)
            intent.putExtra(BRANCH_ID, branchData.id)
            intent.putExtra(BRANCH_TITLE, branchData.title)

            return intent
        }
    }