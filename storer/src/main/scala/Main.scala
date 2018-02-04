package storer


import config.storerconfig._
import model.TweetMessage
import PubSubReader._

object Main extends App {

  import CassandraPersister._

  getConfig() match {
    case Left(error) =>
      System.err.println("config error: " + error)
      System.exit(-1)

    case Right(config) =>
      startPubsubReader(config.google, cassandraPersist)





  }




}
