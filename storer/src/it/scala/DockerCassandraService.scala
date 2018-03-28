package storer

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker}
import scala.concurrent.duration._

trait DockerCassandraService extends DockerKit {

  override val StartContainersTimeout = 40.seconds

  val cassandraContainer: DockerContainer =
    DockerContainer(image = "cassandra:3.11.2")
      .withPorts(9042 -> Some(InitializeTests.cassandraHostPort))
      .withReadyChecker(DockerReadyChecker.LogLineContains("Starting listening for CQL clients on"))

  abstract override def dockerContainers: List[DockerContainer] =
      cassandraContainer :: super.dockerContainers

}