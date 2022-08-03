package com.pedrogomez.hnmob.view.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.view.comments.viewmodel.AuthViewModel
import com.pedrogomez.hnmob.view.comments.viewmodel.CommentsViewModel
import com.pedrogomez.hnmob.viewmodel.SharedHitsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseDataBase = module {
    single {
        Firebase.database.useEmulator("10.0.2.2", 9000)
    }
}

val firebaseAuth = module {
    single {
        Firebase.auth.useEmulator("10.0.2.2", 9099)
    }
}

val commentsViewModel = module {
    viewModel {
        CommentsViewModel(
            get()
        )
    }
}

val authViewModel = module {
    viewModel {
        AuthViewModel(
            get()
        )
    }
}

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