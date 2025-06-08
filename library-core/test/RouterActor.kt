import actor.RouterActor
import actor.Actor
import actorSystem.ActorRef
import kotlin.test.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.delay


class RouterActor {
    @Test
    fun testRouterActorBroadcasts() = runTest {
        val system = ActorSystemImpl()
        val a1 = CollectActor()
        val a2 = CollectActor()
        val r = RouterActor<String>().apply {
            context = object : actorSystem.ActorContext {
                override val system = system
                override val self: ActorRef<*> = system.createActor(logic = { this@apply })
                override val parent: ActorRef<*>? = null
                override fun <T : Any> createChild(name: String?, logic: suspend () -> Actor<T>): ActorRef<T> =
                    system.createActor(name, self, logic)
            }
            addRoutee(system.createActor(logic = { a1 }))
            addRoutee(system.createActor(logic = { a2 }))
        }
        r.onReceive("hello")
        delay(30)
        assertEquals(listOf("hello"), a1.bag)
        assertEquals(listOf("hello"), a2.bag)
        system.shutdown()
    }
}

class CollectActor : Actor<String>() {
    val bag = mutableListOf<String>()
    override suspend fun onReceive(message: String) {
        bag.add(message)
    }
}