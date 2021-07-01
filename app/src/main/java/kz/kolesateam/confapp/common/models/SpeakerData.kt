package kz.kolesateam.confapp.common.models

data class SpeakerData(
    val id: Int,
    val fullName: String,
    val job: String,
    val photoUrl: String,
    var isInvited: Boolean = false
)