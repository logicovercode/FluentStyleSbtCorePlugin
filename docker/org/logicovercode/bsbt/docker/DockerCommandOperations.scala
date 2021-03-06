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
      dockerArgs: String,
      dockerFile: String,
      imageName: String
  ): Unit = {
    import sys.process._

    val buildCommand = s"docker build -t ${imageName} ${dockerArgs} -f $dockerFile $executionDir"
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

  def parseDockerCommandArgs(args: Seq[String]): (String, String, String) = {

    println("all args")
    args.foreach(println)

    val argsParamString = (for {
      argParam <- args.filter(arg => !arg.startsWith("dir") && !arg.startsWith("file"))
      argParamValue = argParam.replace("args=", "")
      argParamWithoutQuote = argParamValue.replace("\"", "")
    } yield argParamWithoutQuote).mkString(" ")

    println(s"arg param string >$argsParamString<")

    val tuples = args.map { arg =>
      val params = arg.split("=")
      params match {
        case Array("dir", dirValue)   => ("dir" -> dirValue)
        case Array("file", fileValue) => ("file" -> fileValue)
        case _                        => ("", "")
      }
    }

    val map = (tuples :+ ("args" -> argsParamString)).filter(!_._1.trim.isEmpty).toMap

    val dockerArgs = map.get("args").getOrElse("")
    val dockerArgsWithBuildArg = dockerArgs.isEmpty match {
      case true  => ""
      case false => dockerArgs.split(" ").map("--build-arg " + _).map(_.trim).mkString(" ")
    }

    val dirArgs = map.get("dir").getOrElse(".")
    val fileArgs = map.get("file").getOrElse("Dockerfile")

    println(s"value for dir >$dirArgs<")
    println(s"value for file >$fileArgs<")
    println(s"value for args >$dockerArgs<, with buildArg >$dockerArgsWithBuildArg<")

    (dirArgs, dockerArgsWithBuildArg, fileArgs)
  }
}
