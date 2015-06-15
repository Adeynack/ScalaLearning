package david.ActorThreadingTest

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, _}

class ConcurrencyTestActor extends Actor with ActorLogging {

  import ConcurrencyTestActor._

  // import context._ // <-- DO NOT DO THIS, otherwise the implicit 'executor' declared next will be bypassed :'(

  implicit protected val executor = MessageBasedExecutor(self, context.dispatcher)

  private val runsLeft = new AtomicInteger(Application.runs)
  private val concurrentFutureExecution = new AtomicInteger(0)

  val service = context.actorOf(Props(new ServiceActor()), "SubService")

  /** Just an example mutable state. */
  var mutableState = 0

  //--------------------------------------------------------------------------------------------------------------------
  // NB: This could be done in 'receive', but then you need to explicitly support 'runnable' in all of your receive
  // methods (when using 'become'/'unbecome').
  //--------------------------------------------------------------------------------------------------------------------
  override def unhandled(message: Any) = message match {
    case message: Runnable => message.run()
    case message: Any => super.unhandled(message)
  }

  override def receive = {

    case DoIt(id, delay, false) =>
      log.info(s"[$id] Received msg : $delay")
      val f = service.ask(DoIt(id, delay, false))(Duration(10, SECONDS)).asInstanceOf[Future[Long]]
      //----------------------------------------------------------------------------------------------------------------
      // The future itself is started by 'ServiceActor', on the global thread pool (AKKA's default context executor).
      // All the 'map' and 'onComplete' are executed with THIS CLASS' implicit executor, which will post them as
      // runnable to 'self' and be executed as messages, so in a thread safe fashion.
      //----------------------------------------------------------------------------------------------------------------
      f.map { millis =>
        checkIfSafelyExecuted(s"[$id] Mapping millis to 1st message") {
          s"It was $millis milliseconds long"
        }
      } map { msg: String =>
        mutableState += 12 // this would be completely thread-UNSAFE without the 'MessageBasedExecutor'.
        checkIfSafelyExecuted(s"[$id] Mapping 1st to 2nd message") {
          s"[[ $msg ]]"
        }
      } onComplete { msg =>
        checkIfSafelyExecuted("OnComplete") {
          Thread.sleep(1000)
          val left: Int = runsLeft.decrementAndGet()
          log.info(s"[$id] Completed msg: $msg $left left.")
          if (left == 0) context.system.shutdown()
        }
      }

  }

  /** Ensures that the @task is not executed during another one in this actor's context. */
  private def checkIfSafelyExecuted[T](description: String)(task: => T): T = {
    val concurrency = concurrentFutureExecution.incrementAndGet()

    val logMessage = s"Executing $description. $concurrency concurrent future(s) running."
    if (concurrency > 1) log.error(logMessage) else log.info(logMessage)

    val result = task
    concurrentFutureExecution.decrementAndGet()
    result
  }

}

object ConcurrencyTestActor {

  case class DoIt(id: Long, delay: Duration, ensureSynced: Boolean)

}
