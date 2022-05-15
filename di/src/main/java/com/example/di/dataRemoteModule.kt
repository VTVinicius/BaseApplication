package com.example.di

import com.example.data_remote.utils.WebServiceFactory
import org.koin.dsl.module

val dataRemoteModule = module {

    single {
        WebServiceFactory.provideOkHttpClient(
            wasDebugVersion = true
        )
    }


    // Conecta com a API que desejar

//    single {
//        WebServiceFactory.createWebService(
//            get(),
//            url = GITHUB_API_URL   <- CONSTANTE DA API
//        ) as GithubWebService      <- WebService que utiliza a API
//    }
//
//    single<GithubRemoteDataSource> { GithubRemoteDataSourceImpl(get()) }
//
}