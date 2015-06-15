package david.ActorThreadingTest

import akka.actor.ActorRef

import scala.concurrent.ExecutionContext

class MessageBasedExecutor(private val target: ActorRef, private val underlyingExecutionContext: ExecutionContext) extends ExecutionContext {

  override def execute(runnable: Runnable) = target ! runnable

  override def reportFailure(cause: Throwable) = underlyingExecutionContext.reportFailure(cause)

}

object MessageBasedExecutor {

  def apply(target: ActorRef, underlyingExecutionContext: ExecutionContext): ExecutionContext =
    new MessageBasedExecutor(target, underlyingExecutionContext)

}