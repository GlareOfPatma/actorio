package actor

import actorSystem.ActorContext
import actorSystem.ActorRef

abstract class Actor<T : Any> {
    lateinit var context: ActorContext

    open suspend fun onStart() {}
    abstract suspend fun onReceive(message: T)
    open suspend fun onStop() {}

    protected suspend fun <U : Any> send(to: ActorRef<U>, msg: U) {
        to.send(msg)
    }
}