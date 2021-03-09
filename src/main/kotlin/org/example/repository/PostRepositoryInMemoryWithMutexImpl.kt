package org.example.repository

import io.ktor.features.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.model.PostModel

class PostRepositoryInMemoryWithMutexImpl : PostRepository {

    private val items = mutableListOf<PostModel>()
    private var nextId = 1L

    private val mutex = Mutex()

    override suspend fun getAll(): List<PostModel> = mutex.withLock {

            // Увеличиваем счетчик просмотров на всех item-ах
            items.forEachIndexed { index, postModel ->
                items[index] = postModel.copy(countViews = postModel.countViews.inc())
            }

            // Возвращаем перевернутый список
            items.reversed()
        }

    override suspend fun getById(id: Long): PostModel = mutex.withLock {

        val item = items.find { it.id == id } ?: throw NotFoundException()

        // Увеличиваем счетчик.
        val index = items.indexOfFirst { it.id == item.id }
        items[index] = item.copy(
            countViews = items[index].countViews + 1
        )

        return items[index]
    }

    override suspend fun save(item: PostModel): PostModel = mutex.withLock {
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

    override suspend fun removeById(id: Long) {
        mutex.withLock {
            items.removeIf { it.id == id }
        }
    }

    override suspend fun likeById(id: Long): PostModel? = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copyItem = item.copy(likedCount = item.likedCount.inc())
                items[index] = copyItem
                copyItem
            }
        }
    }

    override suspend fun dislikeById(id: Long): PostModel? = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copyItem = item.copy(dislikedCount =  item.dislikedCount.inc())
                items[index] = copyItem
                copyItem
            }
        }
    }


    override suspend fun repostById(id: Long): PostModel? = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copyItem = item.copy(
                    repostByMe = true,
                    repostCount = item.repostCount.inc()
                )
                items[index] = copyItem
                copyItem
            }
        }
    }

    override suspend fun shareById(id: Long): PostModel? = mutex.withLock {
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copyItem = item.copy(
                    sharedByMe = true,
                    sharedCount = item.repostCount.inc()
                )
                items[index] = copyItem
                copyItem
            }
        }
    }

    override suspend fun getRecent(): List<PostModel> {
        try {
            if (items.isEmpty()) {
                return emptyList()
            }
            return getAll().slice(0..4)
        } catch (e: IndexOutOfBoundsException) {
            return getAll()
        }
    }

    override suspend fun getPostsAfter(id: Long): List<PostModel>? {
        val item = getById(id)
        val itemsReversed = getAll()
        return when (val index = itemsReversed.indexOfFirst { it.id == item.id }) {
            -1 -> null
            0 -> emptyList()
            else -> itemsReversed.slice(0 until index)
        }
    }

    override suspend fun getPostsBefore(id: Long): List<PostModel>? {
        val item = getById(id)
        val itemsReversed = getAll()
        return when (val index = itemsReversed.indexOfFirst { it.id == item.id }) {
            -1 -> null
            (items.size - 1) -> emptyList()
            else -> {
                try {
                    itemsReversed.slice((index + 1)..(index + 5))
                } catch (e: IndexOutOfBoundsException) {
                    itemsReversed.slice((index + 1) until items.size)
                }
            }
        }
    }
}