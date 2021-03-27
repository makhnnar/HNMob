package com.pedrogomez.hnmob.view.di

import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.view.viewmodel.SharedHitsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productsRepository = module {
    single<SharedHitsViewModel.Repository> {
        HitsProvider(
                get(),
                get()
        )
    }
}

val viewModelListModule = module {
    viewModel {
        SharedHitsViewModel(
            get()
        )
    }
}