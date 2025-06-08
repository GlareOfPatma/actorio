import actor.PoisonPill
import actorSystem.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import supervisor.SupervisorStrategy

class ActorSystemImpl : ActorSystem {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val actors = mutableMapOf<String, Job>()
    private val channels = mutableMapOf<String, Channel<Any>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createActor(
        name: String?,
        parent: ActorRef<*>?,
        logic: ActorLogic<T>,
        supervisorStrategy: SupervisorStrategy
    ): ActorRef<T> {
        val actorName = name ?: "actor-${actors.size}"
        val channel = Channel<T>(Channel.UNLIMITED)
        val ref = LocalActorRef(actorName, channel)
        channels[actorName] = channel as Channel<Any>
        val job = scope.launch {
            val actor = logic()
            val context = object : ActorContext {
                override val system: ActorSystem = this@ActorSystemImpl
                override val self: ActorRef<*> = ref
                override val parent: ActorRef<*>? = parent
                override fun <T : Any> createChild(
                    name: String?,
                    logic: suspend () -> actor.Actor<T>
                ): ActorRef<T> =
                    this@ActorSystemImpl.createActor(name, ref, logic)
            }
            actor.context = context
            actor.onStart()
            try {
                for (msg in channel) {
                    if (msg is PoisonPill) break
                    actor.onReceive(msg)
                }
            } finally {
                actor.onStop()
                channel.close()
            }
        }
        actors[actorName] = job
        return ref
    }

    override fun stopActor(actor: ActorRef<*>) {
        channels[actor.name]?.trySend(PoisonPill)
    }

    override fun shutdown() {
        actors.values.forEach { it.cancel() }
        channels.values.forEach { it.close() }
        actors.clear()
        channels.clear()
        scope.cancel()
    }
}
