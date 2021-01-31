package org.logicovercode.bsbt.docker

import com.whisk.docker.DockerContainer
import sbt.Keys._
import sbt.{Def, _}

trait DockerSettings extends DockerCommandOperations {

  lazy val dependentDockerContainers = settingKey[Set[DockerContainer]]("set of dependent docker containers")
  lazy val startServices = taskKey[Unit]("start dependent containers")
  lazy val buildImage = inputKey[Unit]("build docker image on local with latest tag")
  lazy val tagImageForGitHub = taskKey[Unit]("tag latest docker image to push on github")
  lazy val publishImageToGitHub = taskKey[Unit]("publish image to github")

  lazy val dockerSettings = Seq[Def.Setting[_]](
    dependentDockerContainers := Set(),
    startServices := {
      val containers = dependentDockerContainers.value
      val services = new DockerDependentServices(containers)
      val containersString = containers.map(cnt => cnt.image).mkString(",")
      println("starting services >" + containersString + "<")
      services.startAllOrFail()

      val osName = sys.props.get("os.name").map(_.trim.toUpperCase)

      val sbtProcessId = DockerUtils.pid()
      DockerManagerKiller.killDockerManager(sbtProcessId, osName)
    },

    buildImage := {
      import complete.DefaultParsers._
      val args = spaceDelimited("").parsed
      val _@(executionDir, dockerFile) = parseDockerCommandArgs(args)
      val dockerImageName = imageName(organization.value, name.value)
      buildDockerImageWithLatestTag(executionDir, dockerFile, dockerImageName)

      val tag = s"$dockerImageName:${version.value}"
      tagLatestDockerImageWithVersionTag(dockerImageName, tag)
    },

    tagImageForGitHub := {
      val gitUser = sys.env.get("REPO_GITHUB_USER")

      gitUser match {
        case Some(user) =>
          val dockerImageName = imageName(organization.value, name.value)
          val githubTag = s"ghcr.io/$user/${name.value}:${version.value}"
          tagLatestDockerImageWithVersionTag(dockerImageName, githubTag)

        case None =>
          println(s"environment variable [GIT_USER] is not set")
      }
    },

    publishImageToGitHub := {

      val gitUser = sys.env.get("REPO_GITHUB_USER")

      gitUser match {
        case Some(user) =>
          val githubTag = s"ghcr.io/$user/${name.value}:${version.value}"
          publishDockerImage(githubTag)
        case None =>
          println(s"environment variable [GIT_USER] is not set")
      }
    }
  )
}