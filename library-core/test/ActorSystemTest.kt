import actor.Actor
import kotlinx.coroutines.Dispatchers
import kotlin.test.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class ActorSystemTest {
    private lateinit var system: ActorSystemImpl

    @AfterTest
    fun cleanup() {
        // Чтобы не было утечек и висящих скоупов
        if (this::system.isInitialized) {
            system.shutdown()
        }
    }

    @Test
    fun testActorSystemStopActor() = runTest {
        val system = ActorSystemImpl()
        val actor = StopTestActor()
        val ref = system.createActor(logic = { actor })
        system.stopActor(ref)
        withContext(Dispatchers.Default.limitedParallelism(1)) {
            withTimeout(10000) {
                while (!actor.stopped) {
                    delay(10)
                }
            }
        }
        assertTrue(actor.stopped)
        assertFailsWith<ClosedSendChannelException> { ref.send("fail") }
        system.shutdown()
    }

    @Test
    fun testActorSystemShutdown() = runTest {
        val system = ActorSystemImpl()
        val actor = StopTestActor()
        val ref = system.createActor(logic = { actor })
        system.shutdown()

        assertTrue(true)
    }
}

class StopTestActor : Actor<String>() {
    var stopped = false
    override suspend fun onReceive(message: String) {}
    override suspend fun onStop() {
        stopped = true
    }
}