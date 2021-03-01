package org.example

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    private val jsonContentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)

    @Test
    fun testGetAll() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/api/v1/posts").run {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(jsonContentType, response.contentType())
            }
        }
    }

    @Test
    fun testAdd() {
        withTestApplication({ module() }) {
            with(handleRequest(HttpMethod.Post, "/api/v1/posts") {
                addHeader(HttpHeaders.ContentType, jsonContentType.toString())
                setBody(
                    """
                        {
                            "id": 0,
                            "author": "Vasya",
                            "content": "First post in our network!",
                            "dateStamp": 1585637381,
                            "likedByMe": 1,
                            "likedCount": 1,
                            "sharedByMe": false,
                            "sharedCount": 50,
                            "commentsByMe": false,
                            "commentsCount": 10,
                            "address": "address",
                            "lat": 55.75222,
                            "lng": 37.61556,
                            "postType": "EVENT",
                            "videoLink": null,
                            "intentLink": null,
                            "imageLink": null
                        }
                    """.trimIndent()
                )
            }) {
                response
                assertEquals(HttpStatusCode.OK, response.status())
                assertTrue(response.content!!.contains("\"id\": 1"))
            }
        }
    }
}