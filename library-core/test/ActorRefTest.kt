import actorSystem.LocalActorRef
import kotlinx.coroutines.channels.Channel
import kotlin.test.*

class ActorRefTest {
    @Test
    fun testActorRefEquality() {
        val channel = Channel<String>(Channel.UNLIMITED)
        val ref1 = LocalActorRef("actor1", channel)
        val ref2 = LocalActorRef("actor1", channel)
        val ref3 = LocalActorRef("actor2", channel)
        assertEquals(ref1, ref2)
        assertNotEquals(ref1, ref3)
        assertEquals(ref1.hashCode(), ref2.hashCode())
    }
}