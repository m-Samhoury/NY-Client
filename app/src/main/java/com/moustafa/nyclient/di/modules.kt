package com.moustafa.nyclient.di

import com.moustafa.nyclient.BuildConfig
import com.moustafa.nyclient.repository.Repository
import com.moustafa.nyclient.repository.RepositoryImpl
import com.moustafa.nyclient.repository.network.NYArticlesService
import com.moustafa.nyclient.repository.network.NetworkLayerFactory
import com.moustafa.nyclient.ui.articleslist.ArticlesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */
val repositoryModule: Module = module {
    single { NetworkLayerFactory.makeHttpClient(androidContext()) }

    single<NYArticlesService> { NetworkLayerFactory.makeServiceFactory(get()) }
    single { NetworkLayerFactory.makeRetrofit(BuildConfig.BASE_API_URL, get()) }

    single<Repository> { RepositoryImpl(get()) }
}
val viewModelsModule: Module = module {
    viewModel { ArticlesListViewModel(repository = get()) }
}
