package kz.kolesateam.confapp.di

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.common.preferences.UserNameDataSource
import kz.kolesateam.confapp.common.preferences.SharedPrefsDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

private const val APPLICATION_SHARED_PREFS = "application_shared_prefs"
private const val BASE_URL = "http://37.143.8.68:2020"

val applicationModule: Module = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    single {
        val context = androidContext()

        context.getSharedPreferences(APPLICATION_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    factory<UserNameDataSource> {
        SharedPrefsDataSource(
            sharedPreferences = get()
        )
    }

    single {
        ObjectMapper()
    }

    single {
        NotificationAlarmHelper(
            application = androidApplication()
        )
    }
}