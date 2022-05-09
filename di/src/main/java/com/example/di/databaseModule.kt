package com.example.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

//    single {
//        Room.databaseBuilder(
//            androidContext(),
//            ExampleDatabase::class.java,
//            "user-database"
//        ).build()
//    }
//
//    single { get<ExampleDatabase>().gitUserDao() }

}