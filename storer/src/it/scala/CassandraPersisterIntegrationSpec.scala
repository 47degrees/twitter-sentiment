package storer


import com.datastax.driver.core.Cluster
import com.whisk.docker.impl.spotify.DockerKitSpotify
import io.getquill.{CassandraAsyncContext, SnakeCase}
import model.TweetMessage
import storer.CassandraRepository.cassandraPersist
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures

class CassandraPersisterIntegrationSpec
  extends WordSpec
    with Matchers
    with ScalaFutures
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with DockerTestKit
    with DockerKitSpotify
    with DockerCassandraService {


  //lazy val twitterdb = new CassandraAsyncContext(SnakeCase, "cassandra.twittersentiment")

  lazy val clusterWithoutSSL =
    Cluster.builder()
      .withPort(9043)
      .addContactPoint("localhost")
      .withCredentials("cassandra", "cassandra").build()

  lazy val ctx = new CassandraAsyncContext[SnakeCase](
    naming = SnakeCase,
    cluster = clusterWithoutSSL,
    keyspace = "twittersentiment",
    preparedStatementCacheSize = 100
  )

  import ctx._


  override def beforeAll(): Unit = {
    super.beforeAll()
    // if you want to write more integration test suites and run the migration only once, a different approach is needed
//    val flyway = new Flyway()
//    flyway.setDataSource(postgresUrl, postgresUsername, postgresPassword)
//    flyway.migrate()
  }


  "sentiment repository" when {

    val testTweet = TweetMessage(messageId = "1", data = "This is test data")

    "saving a tweet" should {
      "persist it into the database" in {
        cassandraPersist(ctx).persist(testTweet)

        val q = quote {
          query[TweetMessage].filter(tw => tw.messageId == lift(testTweet.messageId))
        }
        val returnedTweets = ctx.run(q)

        returnedTweets.futureValue.headOption should contain(testTweet)
      }
    }
  }
}

