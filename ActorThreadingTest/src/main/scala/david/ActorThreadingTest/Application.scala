package david.ActorThreadingTest

import akka.actor.{ActorSystem, Props}
import david.ActorThreadingTest.ConcurrencyTestActor.DoIt

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object Application extends App {

  val runs = 10

  private val system = ActorSystem("ActorThreadingTest")
  private val actor = system.actorOf(Props(new ConcurrencyTestActor()), "MainActor")
  private val rnd = new Random()

  for {
    run: Int <- 1 to runs
    delay: Int = rnd.nextInt(10000)
  } actor ! DoIt(run, Duration(delay, MILLISECONDS), false)

  system.awaitTermination()
}
