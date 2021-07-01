package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.events.data.DefaultAllEventsRepository
import kz.kolesateam.confapp.events.data.datasource.AllEventsDataSource
import kz.kolesateam.confapp.common.mappers.EventApiDataMapper
import kz.kolesateam.confapp.events.domain.AllEventsRepository
import kz.kolesateam.confapp.events.presentation.AllEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

val eventScreenModule: Module = module {
    viewModel {
        AllEventsViewModel(
            allEventsRepository = get(),
            favoritesRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single() {
        val retrofit: Retrofit = get()

        retrofit.create(AllEventsDataSource::class.java)
    }

    factory<AllEventsRepository> {
        DefaultAllEventsRepository(
            allEventsDataSource = get(),
            eventApiDataMapper = get()
        )
    }

    factory<EventApiDataMapper> {
        EventApiDataMapper()
    }
}