package kz.kolesateam.confapp.common.preferences

interface UserNameDataSource {

    fun getUserName(): String

    fun saveUserName(
        userName: String
    )

    fun isSavedUserName(): Boolean
}