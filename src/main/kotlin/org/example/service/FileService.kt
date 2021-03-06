package org.example.service

import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.dto.responce.AttachmentResponseDto
import org.example.model.AttachmentType
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class FileService(private val uploadPath: String) {

    private val images = listOf(ContentType.Image.JPEG, ContentType.Image.PNG)

    init {
        // Чекаем есть ли дириктория
        val path = Paths.get(uploadPath)
        if (Files.notExists(path)) {
            Files.createDirectory(path)
        }
    }

    suspend fun save(multipart: MultiPartData): AttachmentResponseDto {

        var response: AttachmentResponseDto? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {

                    // use Apache Tika for content detection
                    if (!images.contains(part.contentType)) {
                        throw UnsupportedMediaTypeException(part.contentType ?: ContentType.Any)
                    }

                    val ext = File(part.originalFileName!!).extension
                    val name = "${UUID.randomUUID()}.$ext"
                    val path = Paths.get(uploadPath, name)

                    part.streamProvider().use {
                        withContext(Dispatchers.IO) {
                            Files.copy(it, path)
                        }
                    }
                    part.dispose()
                    response = AttachmentResponseDto(name, AttachmentType.IMAGE)
                    return@forEachPart
                }
            }
            part.dispose
        }

        return response ?: throw BadRequestException("No file in request")
    }

}