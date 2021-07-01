package kz.kolesateam.confapp.common.listeners

import kz.kolesateam.confapp.common.models.BranchData

interface BranchClickListener {
    fun onBranchClick(
        branchData: BranchData,
    )
}