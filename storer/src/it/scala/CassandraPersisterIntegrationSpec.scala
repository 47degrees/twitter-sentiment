package storer

import com.whisk.docker.impl.spotify.DockerKitSpotify
import model.TweetMessage
import storer.CassandraRepository.cassandraPersist
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures
import InitializeTests._

class CassandraPersisterIntegrationSpec
  extends WordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with DockerTestKit
    with DockerKitSpotify
    with DockerCassandraService {

  import InitializeTests.ctx._

  override def beforeAll(): Unit = {
    super.beforeAll()
    initializeKeyspaces()

  }

  "sentiment repository" when {

    val testTweet = TweetMessage(messageId = "1", data = "This is test data")

    "saving a tweet" should {
      "persist it into the database" in {
        cassandraPersist(ctx).persist(testTweet)

        val q = quote {
          query[TweetMessage].filter(_.messageId == lift(testTweet.messageId))
        }
        val returnedTweets = ctx.run(q)

        returnedTweets.futureValue.headOption should contain(testTweet)
      }
    }
  }
}

