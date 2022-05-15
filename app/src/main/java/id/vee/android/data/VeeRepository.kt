package id.vee.android.data

import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.utils.AppExecutors

class VeeRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : VeeDataSource{

    companion object {
        @Volatile
        private var instance: VeeRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): VeeRepository =
            instance ?: synchronized(this) {
                VeeRepository(remoteDataSource, localDataSource, appExecutors).apply {
                    instance = this
                }
            }
    }
}
}