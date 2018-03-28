package storer

import io.getquill.{CassandraAsyncContext, SnakeCase}
import model.TweetMessage
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Repository[A] {
  def   persist(a: A): Future[Unit]
}

object CassandraRepository {

  def cassandraPersist(ctx: CassandraAsyncContext[SnakeCase]): Repository[TweetMessage] = new Repository[TweetMessage] {
    import ctx._
    override def persist(twmsg: TweetMessage): Future[Unit] = {

      val insertStatement = quote {
        query[TweetMessage].insert(
          _.messageId -> lift(twmsg.messageId),
          _.data -> lift(twmsg.data)
        )
      }
      ctx.run(insertStatement)
    }
  }
}