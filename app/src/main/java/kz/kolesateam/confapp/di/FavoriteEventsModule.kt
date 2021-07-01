package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.favorites.data.DefaultFavoritesRepository
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorites.domain.FavoritesRepository
import kz.kolesateam.confapp.favorites.presentation.FavoriteEventsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val favoriteEventsModule: Module = module {
    viewModel {
        FavoriteEventsViewModel(
            favoritesRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single<FavoritesRepository> {
        DefaultFavoritesRepository(
            context = androidContext(),
            objectMapper = get(),
            eventApiDataMapper = get(),
            favoriteEventActionObservable = get()
        )
    }

    single<FavoriteEventActionObservable> {
        FavoriteEventActionObservable()
    }
}