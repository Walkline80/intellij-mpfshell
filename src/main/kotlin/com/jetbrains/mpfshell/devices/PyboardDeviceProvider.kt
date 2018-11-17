/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.mpfshell.devices

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.mpfshell.run.MpfshellRunConfiguration
import com.jetbrains.mpfshell.settings.MpfshellFacet
import com.jetbrains.mpfshell.settings.MpfshellTypeHints
import com.jetbrains.mpfshell.settings.MpfshellUsbId
import com.jetbrains.mpfshell.settings.mpfshellFacet
import com.jetbrains.python.packaging.PyPackageManager
import com.jetbrains.python.packaging.PyRequirement

/**
 * @author stefanhoelzl
 */
class PyboardDeviceProvider : MpfshellDeviceProvider {
  override val persistentName: String
    get() = "Pyboard"

  override val documentationURL: String
    get() = "https://github.com/vlasovskikh/intellij-micropython/wiki/Pyboard"

  override val usbIds: List<MpfshellUsbId>
    get() = listOf(MpfshellUsbId(0xF055, 0x9800))

  override val typeHints: MpfshellTypeHints by lazy {
    MpfshellTypeHints(listOf("stdlib", "mpfshell", "micropython"))
  }

  override fun getPackageRequirements(sdk: Sdk): List<PyRequirement> {
    val manager = PyPackageManager.getInstance(sdk)
    return manager.parseRequirements("""|pyserial>=3.3,<4.0
                                        |colorama>=0.3.0,<0.3.9
                                        |websocket_client>=0.34.0,<0.52.0""".trimMargin())
  }

  override fun getRunCommandLineState(configuration: MpfshellRunConfiguration,
                                      environment: ExecutionEnvironment): CommandLineState? {
    val facet = configuration.module?.mpfshellFacet ?: return null
    val pythonPath = facet.pythonPath ?: return null
    val devicePath = facet.devicePath ?: return null
    val rootPath = configuration.project.basePath ?: return null
    return object : CommandLineState(environment) {
      override fun startProcess() =
          OSProcessHandler(GeneralCommandLine(pythonPath,
                                              "${MpfshellFacet.scriptsPath}/microupload.py",
                                              "-C",
                                              rootPath,
                                              "-v",
                                              devicePath,
                                              configuration.path))
    }
  }
}