package david.ActorThreadingTest

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import david.ActorThreadingTest.ConcurrencyTestActor.DoIt

import scala.concurrent.Future

class ServiceActor extends Actor with ActorLogging {

  import context.dispatcher

  override def receive = {
    case DoIt(id, delay, false) =>
      Future {
        log.info(s"[$id] Future in the thread pool: $delay")
        val millis = delay.toMillis
        Thread.sleep(millis)
        millis
      } pipeTo sender()
  }

}
