package storer

import com.datastax.driver.core.Cluster
import io.getquill.{CassandraAsyncContext, SnakeCase}
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet

object InitializeTests {

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

  def initializeKeyspaces() = {
    val dataLoader = new CQLDataLoader(clusterWithoutSSL.connect())
    dataLoader.load(new ClassPathCQLDataSet("twittersentiment.cql", true, "twittersentiment"))
  }


}