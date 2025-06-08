package supervisor

sealed class SupervisorStrategy {
    object Restart : SupervisorStrategy()
    object Stop : SupervisorStrategy()
    object Escalate : SupervisorStrategy()
}