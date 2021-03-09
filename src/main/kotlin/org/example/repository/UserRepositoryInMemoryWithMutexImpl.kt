package org.example.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.model.UserModel

class UserRepositoryInMemoryWithMutexImpl : UserRepository {

    private var nextId = 1L
    private val items = mutableListOf<UserModel>()
    private val mutex = Mutex()

    override suspend fun getAll(): List<UserModel> {
        mutex.withLock {
            return items.toList()
        }
    }

    override suspend fun getById(id: Long): UserModel? {
        mutex.withLock {
            return items.find { it.id == id }
        }
    }

    override suspend fun getByIds(ids: Collection<Long>): List<UserModel> {
        mutex.withLock {
            return items.filter { ids.contains(it.id) }
        }
    }

    override suspend fun getByUsername(username: String): UserModel? {
        mutex.withLock {
            return items.find { it.username == username }
        }
    }

    override suspend fun save(item: UserModel): UserModel =
        mutex.withLock {
            when (val index = items.indexOfFirst { it.id == item.id }) {
                -1 -> {
                    val copy = item.copy(id = nextId++)
                    items.add(copy)
                    copy
                }
                else -> {
                    items[index] = item
                    item
                }
            }
        }


    override suspend fun getUserPostsIds(uId: Long): List<Long> {

        TODO("Not yet implemented")
    }

    override suspend fun getUserRepostsIds(uId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getLikesIds(uId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getDislikesIds(uId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getSharedIds(uId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserPost(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserRepost(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveLike(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveDislike(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveShared(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserPostsById(uId: Long, postId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserRepostsById(uId: Long, postId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun removeLikesById(uId: Long, postId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun removeDislikesById(uId: Long, postId: Long): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun removeSharedById(uId: Long, postId: Long): List<Long> {
        TODO("Not yet implemented")
    }
}