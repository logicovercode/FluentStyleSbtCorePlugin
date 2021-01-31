package org.logicovercode.bsbt

import org.logicovercode.bsbt.core.{ModuleIDSettings, ProjectSettings}
import org.logicovercode.bsbt.docker.DockerSettings
import org.logicovercode.bsbt.paths.PluginPaths
import org.logicovercode.bsbt.publishing.PublishingSettings
import org.logicovercode.bsbt.core.ModuleIDSettings
import sbt.AutoPlugin


/**
 * Created by mogli on 4/23/2017.
 */
object BuilderStyleBuild extends AutoPlugin with DockerSettings{

  object autoImport extends PublishingSettings
    with ModuleIDSettings with ProjectSettings with PluginPaths

  override lazy val projectSettings = super.projectSettings ++ dockerSettings
}