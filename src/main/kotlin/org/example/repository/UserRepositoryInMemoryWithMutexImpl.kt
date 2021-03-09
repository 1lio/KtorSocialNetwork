package org.example.repository

import io.ktor.features.*
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

    override suspend fun getUserPostsIds(uId: Long): List<Long> = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        model.createdPosts[uId] ?: listOf()
    }

    override suspend fun getUserRepostsIds(uId: Long): List<Long> = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        model.repostedPost[uId] ?: listOf()
    }

    override suspend fun getLikesIds(uId: Long): List<Long> = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        model.likedPosts[uId] ?: listOf()
    }

    override suspend fun getDislikesIds(uId: Long): List<Long> = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        model.dislikedPosts[uId] ?: listOf()
    }

    override suspend fun getSharedIds(uId: Long): List<Long> = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        model.sharedPosts[uId] ?: listOf()
    }

    override suspend fun saveUserPost(uId: Long, postId: Long): Long = mutex.withLock {

        val model = getById(uId) ?: throw NotFoundException()
        val map = model.createdPosts.toMutableMap()
        val list = map[uId]?.toMutableList() ?: mutableListOf()

        list.add(postId)
        map[uId] = list.toList()

        val newModel = model.copy(createdPosts = map)

        items.remove(model)
        items.add(newModel)

        postId
    }

    override suspend fun saveUserRepost(uId: Long, postId: Long): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveLike(uId: Long, postId: Long): Long = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        val map = model.likedPosts.toMutableMap()
        val list = map[uId]?.toMutableList() ?: mutableListOf()

        list.add(postId)
        map[uId] = list.toList()

        val newModel = model.copy(likedPosts = map)

        items.remove(model)
        items.add(newModel)

        postId
    }

    override suspend fun saveDislike(uId: Long, postId: Long): Long = mutex.withLock {
        val model = getById(uId) ?: throw NotFoundException()
        val map = model.dislikedPosts.toMutableMap()
        val list = map[uId]?.toMutableList() ?: mutableListOf()

        list.add(postId)
        map[uId] = list.toList()

        val newModel = model.copy(dislikedPosts = map)

        items.remove(model)
        items.add(newModel)

        postId
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