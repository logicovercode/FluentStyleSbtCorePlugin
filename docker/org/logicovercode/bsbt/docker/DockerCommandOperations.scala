package org.logicovercode.bsbt.docker

trait DockerCommandOperations {

  final def imageName(organization: String, name: String): String = {
    val arr = (organization).split("[.]")
    println(arr.mkString("::"))
    val org = if (arr.length > 1) arr(1) else organization
    val image = s"${org}/${name}"
    image
  }

  def buildDockerImageWithLatestTag(
      executionDir: String,
      dockerFile: String,
      imageName: String
  ): Unit = {
    import sys.process._

    val buildCommand = s"docker build -t ${imageName} $executionDir"
    println("now building docker image with latest tag :-")
    println(buildCommand)
    buildCommand !
  }

  def tagLatestDockerImageWithVersionTag(
      imageName: String,
      tag: String
  ): Unit = {
    import sys.process._

    val latestTag = s"$imageName:latest"
    println(s"now tagging docker image $latestTag with tag $tag :-")
    val tagCommand = s"docker tag ${latestTag} ${tag}"
    println(tagCommand)
    tagCommand !
  }

  def publishDockerImage(tag: String): Unit = {
    import sys.process._

    println(s"now publishing docker image >$tag<")
    val dockerPushCommand = s"docker push ${tag}"
    println(dockerPushCommand)
    dockerPushCommand !
  }

  def parseDockerCommandArgs(args: Seq[String]): (String, String) = {

    val tuples = args.map { arg =>
      val params = arg.split("=")
      params match {
        case Array("dir", dirValue)  => ("dir" -> dirValue)
        case Array("file", dirValue) => ("file" -> dirValue)
        case _                       => ("", "")
      }
    }

    val map = tuples.filter(!_._1.trim.isEmpty).toMap

    (map.get("dir").getOrElse("."), map.get("file").getOrElse("Dockerfile"))
  }
}
