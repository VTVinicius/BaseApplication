package com.example.data_remote.mapper

abstract class DataRemoteMapper<in R, out D> {
    abstract fun toDomain(data: R): D
}
