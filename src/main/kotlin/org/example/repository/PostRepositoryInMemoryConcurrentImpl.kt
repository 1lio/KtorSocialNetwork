package org.example.repository

import kotlinx.atomicfu.atomic
import org.example.model.PostModel
import java.util.concurrent.CopyOnWriteArrayList

class PostRepositoryInMemoryConcurrentImpl : PostRepository {

    // см. https://github.com/Kotlin/kotlinx.atomicfu#dos-and-donts

    private var nextId = atomic(0L)

    // private val _nextId = atomic(0L)
    // private var nextId by _nextId

    private val items = CopyOnWriteArrayList<PostModel>()

    // Возвращаем все элементы
    // Правда я не понял зачем в обратном порядке
    override suspend fun getAll() = items.reversed()

    // Возращаем по ID
    override suspend fun getById(id: Long): PostModel? = items.find { it.id == id }

    // сохраняем и возвращаем...
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

    // Удаляем по ID
    override suspend fun removeById(id: Long) {
        items.removeIf { it.id == id }
    }

    // Like
    override suspend fun likeById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likedCount = item.likedCount + 1) // copy = id -> id
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
                val copy = item.copy(likedCount = item.likedCount - 1) // copy = id -> id
                items[index] = copy
                copy
            }
        }
}