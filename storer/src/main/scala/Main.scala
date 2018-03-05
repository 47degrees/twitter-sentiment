package storer

import config.storerconfig._
import PubSubReader._
import io.getquill.{CassandraAsyncContext, SnakeCase}

object Main extends App {

  import CassandraRepository._

  getConfig() match {
    case Left(error) =>
      System.err.println("config error: " + error)
      System.exit(-1)

    case Right(config) =>
      val twitterdb: CassandraAsyncContext[SnakeCase] = new CassandraAsyncContext(SnakeCase, "cassandra.twittersentiment")
      startPubsubReader(config.google, cassandraPersist(ctx = twitterdb))

  }

}
