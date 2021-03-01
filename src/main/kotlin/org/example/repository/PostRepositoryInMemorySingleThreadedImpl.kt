package org.example.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.example.model.PostModel

// SINGLE THREADED CONTEXT. Возможность создать однопоточный контекст, модифицируя данные только из этого контекста
// Данный API будет вырезан в будущем

@ExperimentalCoroutinesApi
class PostRepositoryInMemorySingleThreadedImpl : PostRepository {

    private val items = mutableListOf<PostModel>()
    private var nextId = 1L

    private val context = newSingleThreadContext("PostRepository")

    override suspend fun getAll(): List<PostModel> = withContext(context) {
        items.reversed()
    }

    override suspend fun getById(id: Long): PostModel? = withContext(context) {
        items.find { it.id == id }
    }

    override suspend fun save(item: PostModel): PostModel = withContext(context) {
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
        withContext(context) {
            items.removeIf { it.id == id }
        }
    }

    override suspend fun likeById(id: Long): PostModel? = withContext(context) {
        when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likedCount = item.likedCount + 1)
                items[index] = copy
                copy
            }
        }
    }

    override suspend fun dislikeById(id: Long): PostModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun repostById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

    override suspend fun shareById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }
}