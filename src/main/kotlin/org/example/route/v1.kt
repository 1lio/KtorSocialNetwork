package org.example.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.example.dto.PostRequestDto
import org.example.dto.PostResponseDto.Companion.fromModel
import org.example.model.PostModel
import org.example.repository.PostRepository
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

fun Routing.v1() {
    route("/api/v1/posts") {

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

        get("/{id}/like") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.likeById(id) ?: throw NotFoundException()
            val response = fromModel(model)
            call.respond(response)
        }



        post {
            val input = call.receive<PostRequestDto>()
            val model = PostModel(
                id = input.id,
                author = input.author,
                content = input.content,
                dateStamp = input.dateStamp,
                likedByMe = input.likedByMe,
                likedCount = input.likedCount,
                sharedByMe = input.sharedByMe,
                sharedCount = input.sharedCount,
                commentsByMe = input.commentsByMe,
                commentsCount = input.commentsCount,
                postType = input.postType
            )

            val response = fromModel(repo.save(model))
            call.respond(response)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.removeById(id)
        }

    }
}
