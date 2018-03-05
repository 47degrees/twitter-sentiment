package storer

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker}

trait DockerCassandraService extends DockerKit {

  val CassandraAdvertisedPort = 9042

  lazy val cassandraContainer: DockerContainer =
    DockerContainer(image = "cassandra:3.11.2")
      .withPorts(9042 -> Some(9043))
      .withReadyChecker(DockerReadyChecker.LogLineContains("Starting listening for CQL clients on"))
      //.withCommand("ls")

    abstract override def dockerContainers: List[DockerContainer] =
      cassandraContainer :: super.dockerContainers

}