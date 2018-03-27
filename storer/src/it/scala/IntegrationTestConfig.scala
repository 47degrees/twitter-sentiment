package storer

import classy.generic._
import classy.config._
import com.typesafe.config.Config

case class IntegrationConfig(docker: DockerConfig)

case class DockerConfig(dockerCassandraHostPort: Int)

object integrationConfig {

  val decoder = deriveDecoder[Config, IntegrationConfig]
  def getConfig(): Either[_, IntegrationConfig] = decoder.load()

}
