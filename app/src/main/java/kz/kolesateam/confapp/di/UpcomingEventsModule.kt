package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.common.mappers.BranchApiDataMapper
import kz.kolesateam.confapp.upcoming.data.DefaultUpcomingEventsRepository
import kz.kolesateam.confapp.upcoming.data.datasource.UpcomingEventsDataSource
import kz.kolesateam.confapp.upcoming.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.upcoming.presentation.UpcomingEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

val upcomingEventsModule: Module = module {

    viewModel {
        UpcomingEventsViewModel(
            upcomingEventsRepository = get(),
            favoritesRepository = get(),
            notificationAlarmHelper = get(),
            userNameDataSource = get(),
        )
    }

    single() {
        val retrofit: Retrofit = get()

        retrofit.create(UpcomingEventsDataSource::class.java)
    }

    factory<UpcomingEventsRepository> {
        DefaultUpcomingEventsRepository(
            upcomingEventsDataSource = get(),
            branchApiDataMapper = get()
        )
    }

    factory<BranchApiDataMapper> {
        BranchApiDataMapper()
    }
}