package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.details.EventDetailsViewModel
import kz.kolesateam.confapp.details.data.DefaultEventDetailsRepository
import kz.kolesateam.confapp.details.data.datasource.EventDetailsDataSource
import kz.kolesateam.confapp.details.domain.EventDetailsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

val eventDetailsModule: Module = module {
    viewModel {
        EventDetailsViewModel(
            eventDetailsRepository = get(),
            favoritesRepository = get(),
            notificationAlarmHelper = get(),
        )
    }

    single() {
        val retrofit: Retrofit = get()

        retrofit.create(EventDetailsDataSource::class.java)
    }

    factory<EventDetailsRepository> {
        DefaultEventDetailsRepository(
            eventDetailsDataSource = get()
        )
    }
}