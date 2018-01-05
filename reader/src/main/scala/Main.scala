package reader

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

import com.google.pubsub.v1.TopicName
import com.google.pubsub.v1.PubsubMessage
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import cats.syntax.either._

import config.Google
import config.myconfig._

object Main extends App {

  getConfig() match {
    case Left(error) =>
      System.err.println("config error: " + error)
      System.exit(-1)

    case Right(config) =>
      readTweetsIntoPubsub(config.google)
  }


  def readTweetsIntoPubsub(googleconfig: Google): Unit = {


  val trackList = Seq("scala", "47deg", "scalacats", "scalax", "googlecloud", "#scalax", "pubsub",
    "lightbend", "typesafe", "gcp", "47 degrees", "microservices", "kafka", "cassandra",
    "scalaexchange", "tagless", "frees.io", "monads", "categorytheory")

  val topicname = TopicName.of(googleconfig.project, googleconfig.pubsub.topicname)

  val streamingClient = TwitterStreamingClient()
  streamingClient.filterStatuses(stall_warnings = true, tracks = trackList)(publishTweetText)


  def publishTweetText: PartialFunction[StreamingMessage, Unit] = {


    case tweet: Tweet =>
      val response = Either.catchNonFatal(Publisher.defaultBuilder(topicname).build())
      response match {
        case Left(e) => println("WARNING: Got non fatal exception when creating the pubsub publisher. " + e.getMessage)
        case Right(publisher) =>
          println("Sending to " + topicname.toString + " :" +  tweet.text)
          val pubsubmessage: PubsubMessage = PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(tweet.text)).build()
          publisher.publish(pubsubmessage)
      }

  }

  def printTweetText: PartialFunction[StreamingMessage, Unit] = {
    case tweet: Tweet => println(tweet.text)
  }

  }

}
