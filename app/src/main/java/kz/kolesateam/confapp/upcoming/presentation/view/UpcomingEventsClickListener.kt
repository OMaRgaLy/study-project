package kz.kolesateam.confapp.upcoming.presentation.view

import kz.kolesateam.confapp.common.listeners.BranchClickListener
import kz.kolesateam.confapp.common.listeners.EventClickListener
import kz.kolesateam.confapp.common.listeners.FavoritesClickListener

interface UpcomingEventsClickListener : BranchClickListener, EventClickListener,
    FavoritesClickListener