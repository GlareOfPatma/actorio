package actor

import actorSystem.ActorRef

open class RouterActor<T : Any>(
    protected val routees: MutableList<ActorRef<T>> = mutableListOf()
) : Actor<T>() {

    override suspend fun onReceive(message: T) {
        routees.forEach { send(it, message) }
    }

    fun addRoutee(ref: ActorRef<T>) {
        routees.add(ref)
    }

    fun removeRoutee(ref: ActorRef<T>) {
        routees.remove(ref)
    }
}
