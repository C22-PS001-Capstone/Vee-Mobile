package id.vee.android.di

import android.content.Context
import id.vee.android.data.VeeRepository
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.room.VeeDatabase
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiConfig
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.domain.usecase.VeeInteractor
import id.vee.android.domain.usecase.VeeUseCase

object Injection {
    private fun provideRepository(context: Context): VeeDataSource {
        val database = VeeDatabase.getInstance(context)

        val remoteDatasource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val localDatasource = LocalDataSource.getInstance(database.veeDao())
        return VeeRepository.getInstance(remoteDatasource, localDatasource)
    }

    fun provideVeeRepository(context: Context): VeeUseCase {
        val repository = provideRepository(context)
        return VeeInteractor(repository)
    }
}