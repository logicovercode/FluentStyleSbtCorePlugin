package org.logicovercode.bsbt.paths

trait PluginPaths {

  val HOME = {
    sys.env("HOME")
  }
}