package kz.kolesateam.confapp.common.utils

sealed class ProgressState {
    object Loading : ProgressState()
    object Done : ProgressState()
}