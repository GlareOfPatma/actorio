package actorSystem

import actor.Actor

interface ActorContext {
    val system: ActorSystem
    val self: ActorRef<*>
    val parent: ActorRef<*>?
    fun <T : Any> createChild(name: String? = null, logic: suspend () -> Actor<T>): ActorRef<T>
}