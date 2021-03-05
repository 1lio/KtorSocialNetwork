package org.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.server.cio.EngineMain
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.example.repository.PostRepository
import org.example.repository.PostRepositoryInMemoryImpl
import org.example.repository.PostRepositoryInMemoryWithMutexImpl
import org.example.route.v1
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.KodeinFeature

fun main(args: Array<String>): Unit = EngineMain.main(args) // Движок отчечающий за работу

// Тут конфигурируется наш сервер. Для конфигурации Ktor использует фичи.
fun Application.module() {

    // Включаем логирование
    install(CallLogging)

    // Механизм, позволяющий автоматически преобразовывать контент
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting() // Включаем форматирование
            serializeNulls()    // Серилизуем поля, по умолчанию gson пропускает данные поля
        }
    }

    // Обрабатывам ошибки
    install(StatusPages) {
        exception<NotImplementedError> {
            call.respond(HttpStatusCode.NotImplemented)
            throw it
        }

        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
            throw it
        }

        // NotFoundException (ошибка 404)
        exception<NotFoundException> {
            call.respond(HttpStatusCode.NotFound)
            throw it
        }

        // ParameterConversionException (ошибка 400)
        exception<ParameterConversionException> {
            call.respond(HttpStatusCode.BadRequest)
            throw it
        }
    }

    // Внедряем DI
    install(KodeinFeature) {
        // В блок лямбды подкладываем реализацию
        bind<PostRepository>() with singleton { PostRepositoryInMemoryWithMutexImpl() }
    }

    // Подключаем Routing
    install(Routing) {
        v1()
    }

    println("Application is started")
}
