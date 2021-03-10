package org.example.service

import io.ktor.features.*
import org.example.dto.request.PostRequestDto
import org.example.dto.request.RepostRequestDto
import org.example.dto.responce.PostResponseDto
import org.example.exception.ForbiddenException
import org.example.model.PostModel
import org.example.model.UserModel
import org.example.repository.PostRepository
import org.example.repository.UserRepository

class PostService(private val postRepo: PostRepository, private val userRepo: UserRepository) {

    suspend fun getAll(): List<PostResponseDto> = postRepo.getAll().map(PostResponseDto.Companion::fromModel)

    suspend fun getById(id: Long): PostResponseDto {

        val model = postRepo.getById(id) ?: throw NotFoundException()

        // Чекаем пользовательский ли пост
        val userData = userRepo.getById(model.id)

        // В ответе формируем лист с пользовательскими лайками/дизами/репостами и т.п.
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

        // Тащим из БД модель
        val lastPost = postRepo.getAll().size.toLong()

        userRepo.saveUserPost(model.authorId, lastPost)

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

    suspend fun likeById(uId: Long, id: Long): PostResponseDto {

        // находим юзера
        val user = userRepo.getById(uId) ?: throw NotFoundException()

        // Находим модель
        val model = postRepo.getById(id) ?: throw NotFoundException()

        // Чекаем лайки юзера
        val userLikedPosts: List<Long> = user.likedPosts

        val copyModel: PostModel
        // Лайкал ли он хотябы раз
        if (userLikedPosts.isNullOrEmpty()) {
            // Сохраняем лайк в репозиторий пользователя
            copyModel = model.copy(likedByMe = 1, likedCount = model.likedCount.inc())
            userRepo.saveLike(uId, id)
        } else {
            // Ищем среди лакнутых постов наш
            if (userLikedPosts.first { id == it } != 0L) {
                // Снимаем лайк
                copyModel = model.copy(likedByMe = 0, likedCount = model.likedCount.dec())
                userRepo.removeLikesById(uId, id)

            } else {
                // Сохраняем лайк
                copyModel = model.copy(likedByMe = 1, likedCount = model.likedCount.inc())
                userRepo.saveLike(uId, id)
            }

        }

        // Сохраняем лайк в репозиторий
        postRepo.save(copyModel)
        return PostResponseDto.fromModel(copyModel)
    }


    suspend fun dislikeById(uId: Long, id: Long): PostResponseDto {
        val model = postRepo.dislikeById(uId, id) ?: throw NotFoundException()
        return PostResponseDto.fromModel(model)
    }

    suspend fun repostById(id: Long, user: UserModel, repostRequestDto: RepostRequestDto): PostResponseDto {
        //  val reposted = postRepo.getById(id)
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