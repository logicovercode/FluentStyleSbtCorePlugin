package org.logicovercode.bsbt.docker

trait DockerManagerKiller {

  def killDockerManager(
      sbtProcessId: Long,
      osNameOption: Option[String]
  ): Unit = {
    val msg =
      s"attempt to silent process(that is starting docker services) with pid  >$sbtProcessId<"

    osNameOption match {
      case Some("MAC OS X") =>
        println(msg + " on mac")
        killOnUnixBaseBoxes(sbtProcessId)

      case Some(otherOs) =>
        println(
          msg + s" on $otherOs is not yet implemented." + "\n" +
            "Please raise a ticket with this message, and we will implement it with in 24 hours :)"
        )

      case None => println("no os found. this should be reported")
    }
  }

  def killOnUnixBaseBoxes(processId: Long): Int = {
    println(s"kill -9 $processId")

    import sys.process._

    s"kill -9 $processId" !
  }

  def killOnWindows(processId: Long): Int = {
    println(s"TASKKILL /F /PID $processId")

    import sys.process._

    s"TASKKILL /F /PID $processId" !
  }
}

object DockerManagerKiller extends DockerManagerKiller
