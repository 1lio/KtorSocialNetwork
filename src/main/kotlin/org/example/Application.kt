package org.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import org.example.repository.PostRepository
import org.example.repository.PostRepositoryInMongodb
import org.example.route.v1
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.with
import org.kodein.di.ktor.KodeinFeature
import javax.naming.ConfigurationException

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

        // Предоставлемые констаны Kodein-ом
   /*     constant(tag = "upload-dir") with (
                environment.config.propertyOrNull("ncrafr.upload.dir")?.getString()
                    ?: throw ConfigurationException("Upload dir is not specified")
                )*/

        // В блок лямбды подкладываем реализацию
        bind<PostRepository>() with eagerSingleton { PostRepositoryInMongodb() }

        //bind<RoutingV1>() with eagerSingleton { RoutingV1(instance(tag = "upload-dir")) }
    }

    // Подключаем Routing
    install(Routing) {
        v1()
        // val routingV1 by kodein().instance<RoutingV1>()
        // routingV1.setup(this)
    }



    println("Application is started")
}
