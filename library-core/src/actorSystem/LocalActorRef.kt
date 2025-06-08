package actorSystem

import kotlinx.coroutines.channels.Channel

class LocalActorRef<T : Any>(
    override val name: String,
    private val channel: Channel<T>
) : ActorRef<T> {
    override suspend fun send(message: T) {
        channel.send(message)
    }
    override fun equals(other: Any?): Boolean = other is LocalActorRef<*> && other.name == name
    override fun hashCode(): Int = name.hashCode()
}