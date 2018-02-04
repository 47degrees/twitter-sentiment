package storer

import io.getquill.{CassandraAsyncContext, SnakeCase}
import model.TweetMessage
import scala.concurrent.ExecutionContext.Implicits.global

trait Persister[A] {
  def persist(a: A): Unit
}

object CassandraPersister {

  val twitterdb = new CassandraAsyncContext(SnakeCase, "twittersentiment")
  import twitterdb._

  val cassandraPersist: Persister[TweetMessage] = new Persister[TweetMessage] {
    override def persist(twmsg: TweetMessage): Unit = {

      println("Persisting: ", twmsg)
      val insertop = quote {
        query[TweetMessage].insert(
          _.messageId -> lift(twmsg.messageId),
          _.data -> lift(twmsg.data)
        )
      }
      twitterdb.run(insertop)
    }
  }
}