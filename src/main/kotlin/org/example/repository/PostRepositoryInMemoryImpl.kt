package org.example.repository

import org.example.model.PostModel

// В лоб в памяти
class PostRepositoryInMemoryImpl : PostRepository {

    private val items = mutableListOf<PostModel>()      // Список с постами
    private var nextId = 1L                             // Индификатор след ID. Не понял для чего он, но путь остается

    override suspend fun getAll(): List<PostModel> {

        // ОБНОВЛЕНИЕ ПРОСМОТРОВ. Решение в лоб. Тут нужно подумать
        items.forEach {
            it.countViews++
        }

        return items
    }

    override suspend fun getById(id: Long): PostModel? = items.find { it.id == id }

    // id = 0 - создание, id != 0 - обновление
    override suspend fun save(item: PostModel): PostModel =
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

    override suspend fun removeById(id: Long) {
        items.removeIf { it.id == id }
    }

    override suspend fun likeById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(

                    // Чекаем лайки
                    likedCount = item.likedCount + 1,
                    likedByMe = if (item.likedByMe < 1) 1 else 0
                )
                items[index] = copy
                copy
            }
        }

    override suspend fun dislikeById(id: Long): PostModel? =
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(
                    likedCount = item.likedCount - 1,
                    likedByMe = if (item.likedByMe > 0) -1 else 0
                )
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