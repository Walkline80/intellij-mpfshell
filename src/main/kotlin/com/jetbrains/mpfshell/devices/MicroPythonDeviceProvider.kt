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
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.mpfshell.run.MicroPythonRunConfiguration
import com.jetbrains.mpfshell.settings.MicroPythonTypeHints
import com.jetbrains.mpfshell.settings.MicroPythonUsbId
import com.jetbrains.python.packaging.PyRequirement

/**
 * @author vlan
 */
interface MicroPythonDeviceProvider {
  companion object {
    private val EP_NAME: ExtensionPointName<MicroPythonDeviceProvider> =
        ExtensionPointName.create("com.jetbrains.mpfshell.deviceProvider")

    val providers: Array<MicroPythonDeviceProvider>
      get() = Extensions.getExtensions(EP_NAME)

    val default: MicroPythonDeviceProvider
      get() = providers.first { it.isDefault }
  }

  val persistentName: String

  val documentationURL: String

  val usbIds: List<MicroPythonUsbId>
    get() = emptyList()

  val presentableName: String
    get() = persistentName

  fun getPackageRequirements(sdk: Sdk): List<PyRequirement> = emptyList()

  val typeHints: MicroPythonTypeHints?
    get() = null

  val detectedModuleNames: Set<String>
    get() = emptySet()

  fun getRunCommandLineState(configuration: MicroPythonRunConfiguration,
                             environment: ExecutionEnvironment): CommandLineState? = null

  val isDefault: Boolean
    get() = false
}