package com.jetbrains.mpfshell.devices

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.jetbrains.mpfshell.run.MpfshellRunConfiguration
import com.jetbrains.mpfshell.settings.MpfshellFacet
import com.jetbrains.mpfshell.settings.MpfshellTypeHints
import com.jetbrains.mpfshell.settings.MpfshellUsbId
import com.jetbrains.mpfshell.settings.mpfshellFacet
import com.jetbrains.python.packaging.PyPackageManager
import com.jetbrains.python.packaging.PyRequirement

/**
 * @author vlan
 */
class Esp8266DeviceProvider : MpfshellDeviceProvider {
  override val persistentName: String
    get() = "ESP8266"

  override val documentationURL: String
    get() = "https://github.com/vlasovskikh/intellij-micropython/wiki/ESP8266"

  override val usbIds: List<MpfshellUsbId>
    get() = listOf(MpfshellUsbId(0x1A86, 0x7523),
                   MpfshellUsbId(0x10C4, 0xEA60))

  override val typeHints: MpfshellTypeHints by lazy {
    MpfshellTypeHints(listOf("stdlib", "mpfshell", "esp8266", "micropython"))
  }

  override fun getPackageRequirements(sdk: Sdk): List<PyRequirement> {
    val manager = PyPackageManager.getInstance(sdk)
    return manager.parseRequirements("""|pyserial>=3.3,<4.0
                                        |colorama>=0.3.0,<0.3.9
                                        |websocket_client>=0.34.0,<0.38.0""".trimMargin())
  }

  override fun getRunCommandLineState(configuration: MpfshellRunConfiguration,
                                      environment: ExecutionEnvironment): CommandLineState? {
    val module = configuration.module ?: return null
    val facet = module.mpfshellFacet ?: return null
    val pythonPath = facet.pythonPath ?: return null
    val devicePath = facet.devicePath ?: return null
    val rootDir = configuration.project.baseDir ?: return null
    val file = StandardFileSystems.local().findFileByPath(configuration.path) ?: return null
    val excludeRoots = ModuleRootManager.getInstance(module).excludeRoots
    val excludes = excludeRoots
        .asSequence()
        .filter { VfsUtil.isAncestor(file, it, false) }
        .map { VfsUtilCore.getRelativePath(it, rootDir) }
        .filterNotNull()
        .map { listOf("-X", it) }
        .flatten()
        .toList()
    val command = listOf(pythonPath, "${MpfshellFacet.scriptsPath}/microupload.py", "-C", rootDir.path) +
        excludes + listOf("-v", devicePath, configuration.path)

    return object : CommandLineState(environment) {
      override fun startProcess() =
          OSProcessHandler(GeneralCommandLine(command))
    }
  }
}