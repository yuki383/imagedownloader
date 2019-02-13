package jp.ed.nnn.imagedownloader

import java.io.File

import akka.actor.{ActorSystem, Inbox, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  // TODO please fix to your configuration
  val wordsFilePath = ".../workspace/imagedawnloaderUtils/util/words.txt"
  val urlsFilePath = ".../workspace/imagedawnloaderUtils/util/fall11_urls.txt"
  val outputDirPath = ".../workspace/imagedawnloaderUtils/images"
  val numOfDownloader = 20
  val config = Config(
    wordsFilePath,
    urlsFilePath,
    outputDirPath,
    numOfDownloader)


  val system = ActorSystem("imagedownloader",
    ConfigFactory.parseFile(new File("application.conf")))
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val supervisor = system.actorOf(Props(new Supervisor(config)))
  supervisor ! Start

  inbox.receive(100.days)
  Await.ready(system.terminate(), Duration.Inf)
  println("Finished.")
}
