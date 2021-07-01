package kz.kolesateam.confapp.common.listeners

import kz.kolesateam.confapp.common.models.EventData

interface EventClickListener {
    fun onEventClick(
        eventData: EventData,
    )
}