package id.vee.android.data

import id.vee.android.data.local.entity.RemoteKeysEntity
import id.vee.android.data.local.room.RemoteKeysDao

class FakeRemoteKeysDao: RemoteKeysDao {
    override suspend fun insertAll(remoteKey: List<RemoteKeysEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeysEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRemoteKeys() {
        TODO("Not yet implemented")
    }
}