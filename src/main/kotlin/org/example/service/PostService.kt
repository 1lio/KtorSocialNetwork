package org.example.service

import io.ktor.features.*
import org.example.dto.request.PostRequestDto
import org.example.dto.request.RepostRequestDto
import org.example.dto.responce.PostResponseDto
import org.example.exception.ForbiddenException
import org.example.model.UserModel
import org.example.repository.PostRepository
import org.example.repository.UserRepository

class PostService(private val postRepo: PostRepository, private val userRepo: UserRepository) {

    suspend fun getAll(): List<PostResponseDto> = postRepo.getAll().map(PostResponseDto.Companion::fromModel)

    suspend fun getById(id: Long): PostResponseDto {

        val model = postRepo.getById(id) ?: throw NotFoundException()

        // Чекаем пользовательский ли пост
        val userData = userRepo.getById(model.id)

        return PostResponseDto.fromModel(model)
    }

    // Сохранение / изменение
    suspend fun save(request: PostRequestDto, user: UserModel): PostResponseDto {

        // Чекаем создан ли Post пользователем.
        if (request.authorId != user.id) {
            throw ForbiddenException("Access deny!")
        }

        // Сохраняем в репозиторий
        val model = PostRequestDto.toModel(request)
        postRepo.save(model)
        return PostResponseDto.fromModel(model)
    }

    suspend fun deleteById(id: Long, user: UserModel): PostResponseDto {

        // Ищем Post
        val model = postRepo.getById(id) ?: throw NotFoundException()

        // Чекаем создан ли post пользователем. По умочанию удалять может только пользователь создавший пост.
        if (model.authorId != user.id) {
            throw ForbiddenException("Access deny!")
        }
        postRepo.removeById(id)
        return PostResponseDto.fromModel(model)
    }

    suspend fun likeById(id: Long): PostResponseDto {
        // Ищем пост
        val model = postRepo.likeById(id) ?: throw NotFoundException()

        // Сохраняем наш лайк
       // userRepo.saveLike(userRepo.)

        return PostResponseDto.fromModel(model)
    }

    suspend fun dislikeById(id: Long): PostResponseDto {
        val model = postRepo.dislikeById(id) ?: throw NotFoundException()
        return PostResponseDto.fromModel(model)
    }

    suspend fun repostById(id: Long, user: UserModel, repostRequestDto: RepostRequestDto): PostResponseDto {
        val reposted = postRepo.getById(id)
        /* val newPostForSave = PostModel(
             id = -1,
             authorId = user.id,
             content = repostRequestDto.content,
             created = System.currentTimeMillis(),
             postType = PostType.REPOST,
             // source = reposted
         )*/
        //   val repost = repo.save(newPostForSave)
        TODO("//")
        //return PostResponseDto.fromModel(repost)
    }


}