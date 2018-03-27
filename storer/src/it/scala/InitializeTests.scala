package storer

import com.datastax.driver.core.Cluster
import io.getquill.{CassandraAsyncContext, SnakeCase}
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import storer.integrationConfig._
import cats.implicits._

object InitializeTests {


  val cassandraHostPort = getConfig.leftMap{ error =>
    System.err.println("config error: " + error)
    System.exit(-1)
  }.map { config => config.docker.dockerCassandraHostPort}.getOrElse(9044)

  lazy val clusterWithoutSSL =
    Cluster.builder()
      .withPort(cassandraHostPort)
      .addContactPoint("localhost")
      .withCredentials("cassandra", "cassandra").build()

  lazy val ctx = new CassandraAsyncContext[SnakeCase](
    naming = SnakeCase,
    cluster = clusterWithoutSSL,
    keyspace = "twittersentiment",
    preparedStatementCacheSize = 100
  )

  def initializeKeyspaces() = {
    val dataLoader = new CQLDataLoader(clusterWithoutSSL.connect())
    dataLoader.load(new ClassPathCQLDataSet("twittersentiment.cql", true, "twittersentiment"))
  }


}