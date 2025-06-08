package actorSystem

import supervisor.SupervisorStrategy

typealias ActorLogic<T> = suspend () -> actor.Actor<T>

interface ActorSystem {
    fun <T : Any> createActor(
        name: String? = null,
        parent: ActorRef<*>? = null,
        logic: ActorLogic<T>,
        supervisorStrategy: SupervisorStrategy = SupervisorStrategy.Restart
    ): ActorRef<T>
    fun stopActor(actor: ActorRef<*>)
    fun shutdown()
}