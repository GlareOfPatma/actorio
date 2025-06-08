import actor.Actor
import kotlinx.coroutines.*
import mu.KotlinLogging
import kotlin.system.measureTimeMillis

private val log = KotlinLogging.logger {}

class LoggingActor(private val id: Int) : Actor<Int>() {
    override suspend fun onStart() {
        log.info { "Actor #$id started" }
    }

    override suspend fun onReceive(message: Int) {
        // эмулируем небольшую работу
        delay(2)
        log.debug { "Actor #$id received message: $message" }
    }

    override suspend fun onStop() {
        log.info { "Actor #$id stopped" }
    }
}

suspend fun main() {
    val system = ActorSystemImpl()

    val actorCount = 500
    val messagesPerActor = 100

    val actors = (1..actorCount).map { id ->
        system.createActor(name = "loggingActor-$id", logic = { LoggingActor(id) })
    }

    log.info { "All actors created, sending messages..." }

    val elapsed = measureTimeMillis {
        coroutineScope {
            actors.forEach { ref ->
                launch {
                    repeat(messagesPerActor) { msg ->
                        ref.send(msg)
                    }
                }
            }
        }
    }

    log.info { "All messages sent in ${elapsed}ms. Now stopping actors..." }

    actors.forEach { ref ->
        system.stopActor(ref)
    }

    log.info { "Shutting down actor system..." }
    system.shutdown()

    log.info { "Load test finished." }
}
