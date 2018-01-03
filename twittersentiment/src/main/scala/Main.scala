import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

object Main extends App {
  println("Hello, World!")

  val trackList = Seq("scala", "47deg", "scalacats", "scalax",
    "googlecloud", "#scalax", "pubsub",
    "lightbend", "typesafe", "gcp",
    "scalability", "reactive", "microservices", "kafka", "cassandra",
    "scalaexchange", "tagless", "freestyle", "monads", "categorytheory")

  val streamingClient = TwitterStreamingClient()
  streamingClient.filterStatuses(stall_warnings = true, tracks = trackList)(printTweetText)

  def printTweetText: PartialFunction[StreamingMessage, Unit] = {
    case tweet: Tweet => println(tweet.text)
  }


}
