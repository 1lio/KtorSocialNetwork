package org.example.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.example.dto.PostRequestDto
import org.example.dto.PostResponseDto.Companion.fromModel
import org.example.model.PostModel
import org.example.model.PostType
import org.example.repository.PostRepository
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

const val BASE_URL = "/api/v1"

fun Routing.v1() {

    // REST
    route("$BASE_URL/posts") {

        val repo by kodein().instance<PostRepository>()

        get {
            val response = repo.getAll().map { fromModel(it) }
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.getById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }

        post("/{id}/like") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.likeById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }

        post("/{id}/dislike") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.dislikeById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }

        post("/{id}/repost") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.repostById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }

        post("/{id}/share") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.shareById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }


        post {
            val input = call.receive<PostRequestDto>()
            val model = PostModel(
                id = input.id,
                author = input.author!!,
                content = input.content!!,
                created = input.created!!,
                location = input.location!!,
                //  likedByMe = input.,
                // likedCount = input.likedCount,
                // sharedByMe = input.sharedByMe,
                //  sharedCount = input.sharedCount,
                // commentsByMe = input.commentsByMe,
                //commentsCount = input.commentsCount,
                postType = input.postType ?: PostType.POST,
                //  dislikedCount = input.dislikedCount
            )

            val response = fromModel(repo.save(model))
            call.respond(response)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.removeById(id)
        }

    }

    // Статический контент
    static("$BASE_URL/static") {
        //  files(staticPath)
    }

}
