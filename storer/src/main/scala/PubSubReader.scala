package storer

import java.util.concurrent.{BlockingQueue, LinkedBlockingDeque}
import com.google.cloud.pubsub.v1.{AckReplyConsumer, MessageReceiver, Subscriber}
import com.google.pubsub.v1.{PubsubMessage, SubscriptionName}
import config.Google
import model.TweetMessage
import cats.syntax.either._

object PubSubReader {

  val messages: BlockingQueue[PubsubMessage] = new LinkedBlockingDeque()

  val receiver: MessageReceiver = new MessageReceiver() {
    override def receiveMessage(message: PubsubMessage, consumer: AckReplyConsumer) {
      messages.offer(message)
      consumer.ack()
    }
  }


  def startPubsubReader(googleconfig: Google, persister: Repository[TweetMessage]): Unit = {

    val subscriptionName: SubscriptionName = SubscriptionName.of(googleconfig.project, googleconfig.pubsub.subscriptionname);
    val response = Either.catchNonFatal(Subscriber.newBuilder(subscriptionName, receiver).build())

    response match {
      case Left(e) => System.err.println(s"WARNING: Got non fatal exception when creating the pubsub subscriber. ${e.getMessage}")
      case Right(subscriber) =>
        subscriber.startAsync().awaitRunning()
        while(true) {
          val pubsubmsg: PubsubMessage = messages.take()
          val msg_text = pubsubmsg.getData().toStringUtf8()
          System.out.println(s"Saving in the Database: ${msg_text}")
          val tweetmsg = TweetMessage(pubsubmsg.getMessageId(), msg_text)
          persister.persist(tweetmsg)
        }
    }
    //TODO: As shown in https://cloud.google.com/pubsub/docs/quickstart-client-libraries#pubsub-quickstart-publish-java, we should do a subscriber.stopAsync()

  }
}
