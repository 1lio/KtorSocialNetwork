package org.example.repository

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.model.PostModel
import java.util.concurrent.CopyOnWriteArrayList

// Паралельность
class PostRepositoryInMemoryConcurrentImpl : PostRepository {

    // см. https://github.com/Kotlin/kotlinx.atomicfu#dos-and-donts

    private val items = CopyOnWriteArrayList<PostModel>()
    private var nextId = atomic(0L)

    override suspend fun getAll() = items.reversed()

    override suspend fun getById(id: Long): PostModel? = items.find { it.id == id }

    override suspend fun save(item: PostModel): PostModel =
        when (val index = items.indexOfFirst { it.id == item.id }) {
            -1 -> {
                val copy = item.copy(id = nextId.incrementAndGet())
                items.add(copy)
                copy
            }
            else -> {
                items[index] = item
                item
            }
        }

    override suspend fun removeById(id: Long) {
        items.removeIf { it.id == id }
    }

    // Like
    override suspend fun likeById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likedCount = item.likedCount.inc()) // copy = id -> id
                items[index] = copy
                copy
            }
        }

    // Hate
    override suspend fun dislikeById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likedCount = item.dislikedCount.dec())
                items[index] = copy
                copy

            }
        }

    override suspend fun repostById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(
                    repostByMe = true,
                    repostCount = item.repostCount.inc()
                )
                items[index] = copy
                copy
            }
        }

    override suspend fun shareById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(
                    sharedByMe = true,
                    sharedCount = item.repostCount.inc()
                )
                items[index] = copy
                copy
            }
        }
}