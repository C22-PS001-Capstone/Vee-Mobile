package id.vee.android.di

import android.content.Context
import id.vee.android.data.VeeDataSource
import id.vee.android.data.VeeRepository
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.room.VeeDatabase
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiConfig
import id.vee.android.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): VeeDataSource {
        val database = VeeDatabase.getInstance(context)

        val remoteDatasource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val localDatasource = LocalDataSource.getInstance(database.userDao())
        val appExecutors = AppExecutors()
        return VeeRepository.getInstance(remoteDatasource, localDatasource, appExecutors)
    }
}