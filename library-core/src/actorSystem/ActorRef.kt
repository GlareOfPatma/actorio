package actorSystem

interface ActorRef<T : Any> {
    val name: String
    suspend fun send(message: T)
}