package org.example.repository

import org.example.model.PostModel
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

// в разработке. Не успеваю.

class PostRepositoryInMongodb : PostRepository {

    private val client = KMongo.createClient().coroutine
    private val db = client.getDatabase(DB_NAME)

    private val list = db.getCollection<PostModel>()
    private var nextId = 1L

    override suspend fun getAll(): List<PostModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): PostModel? {
        return list.findOne(PostModel::id eq id)
            ?: throw Exception("Not a found: $id")
    }

    override suspend fun save(item: PostModel): PostModel {
        list.save(item)
        return list.findOne(PostModel::id eq item.id)
            ?: throw Exception("Not a found: ${item.id}")
    }

    override suspend fun removeById(id: Long) {
        list.dropIndex(PostModel::id eq id)
            ?: throw Exception("Not a found: $id")
    }

    override suspend fun likeById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

    override suspend fun dislikeById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

    override suspend fun repostById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

    override suspend fun shareById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

    private companion object {
        const val DB_NAME = "post_repository"
    }
}