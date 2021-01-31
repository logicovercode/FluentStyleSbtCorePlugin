package org.logicovercode.bsbt.docker

import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.netty.NettyDockerCmdExecFactory
import com.whisk.docker.impl.dockerjava.{Docker, DockerJavaExecutorFactory}
import com.whisk.docker.{DockerContainer, DockerFactory, DockerKit}
import org.slf4j.LoggerFactory
import sbt.Def

trait IDockerContainer{
  def instance() : DockerContainer
  def settings() : Set[Def.Setting[_]]
}

class DockerDependentServices(_dockerContainers: Set[DockerContainer])
  extends DockerKit {

  import scala.concurrent.duration._  

  override val StartContainersTimeout = 2.minutes

  override def dockerContainers: List[DockerContainer] = _dockerContainers.toList

  private lazy val log = LoggerFactory.getLogger(this.getClass)

  override implicit val dockerFactory: DockerFactory = new DockerJavaExecutorFactory(
    new Docker(DefaultDockerClientConfig.createDefaultConfigBuilder().build(),
      factory = new NettyDockerCmdExecFactory()))
}
