package com.example.di

import org.koin.dsl.module

val dataRemoteModule = module {

//    single {
//        WebServiceFactory.provideOkHttpClient(
//            wasDebugVersion = true
//        )
//    }
//
//    single {
//        WebServiceFactory.createWebService(
//            get(),
//            url = GITHUB_API_URL
//        ) as GithubWebService
//    }
//
//    single<GithubRemoteDataSource> { GithubRemoteDataSourceImpl(get()) }
//
}