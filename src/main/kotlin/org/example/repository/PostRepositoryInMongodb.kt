package org.example.repository

import io.ktor.features.*
import org.example.config.Constants.DB_NAME
import org.example.model.PostModel
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

// Вообщем данное решение работает, на локальной машине с установленной монгой
// оОднако с деплоем на хероку не все так просто...

// ---

// Необходимо установить Mongodb https://www.mongodb.com/try/download/community
// Создать либо локальную базу, либо на серверах монги https://cloud.mongodb.com/
// Сконфигурировать в проекте   https://litote.org/kmongo/
// Сконфигурировать на сервере(Heroku) https://devcenter.heroku.com/articles/procfile

class PostRepositoryInMongodb : PostRepository {

    private val client = KMongo.createClient().coroutine
    private val db = client.getDatabase(DB_NAME)
    private val list = db.getCollection<PostModel>()

    override suspend fun getAll(): List<PostModel> {
        return list.find().toList()
    }

    override suspend fun getById(id: Long): PostModel {
        return list.findOne(PostModel::id eq id)
            ?: throw NotFoundException()
    }

    override suspend fun save(item: PostModel): PostModel {
        // Тут нужно пофиксить
        list.save(item)
        return list.findOne(PostModel::id eq item.id) ?: throw  NotFoundException()
    }

    override suspend fun removeById(id: Long) {
        list.deleteOne(PostModel::id eq id)
    }

    override suspend fun likeById(id: Long): PostModel? {
        val item = list.findOne(PostModel::id eq id)
        return try {
            val copy = item!!.copy(
                // Чекаем лайки
                likedCount = item.likedCount + 1,
                likedByMe = if (item.likedByMe < 1) 1 else 0
            )
            save(copy)
            copy
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun dislikeById(id: Long): PostModel? {
        val item = list.findOne(PostModel::id eq id)
        return try {
            item!!.copy(
                likedCount = item.likedCount - 1,
                likedByMe = if (item.likedByMe > 0) -1 else 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun repostById(id: Long): PostModel? {
        val item = list.findOne(PostModel::id eq id)
        return try {
            item!!.copy(
                repostByMe = true,
                repostCount = item.repostCount.inc()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun shareById(id: Long): PostModel? {
        val item = list.findOne(PostModel::id eq id)
        return try {
            item!!.copy(
                sharedByMe = true,
                sharedCount = item.repostCount.inc()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
