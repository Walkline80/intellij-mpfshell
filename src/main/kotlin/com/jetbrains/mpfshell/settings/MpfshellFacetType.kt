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

package com.jetbrains.mpfshell.settings

import com.intellij.facet.Facet
import com.intellij.facet.FacetType
import com.intellij.facet.FacetTypeId
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.util.IconLoader
import com.jetbrains.python.PythonModuleTypeBase
import javax.swing.Icon

/**
 * @author vlan
 */
class MpfshellFacetType : FacetType<MpfshellFacet, MpfshellFacetConfiguration>(ID, STRING_ID, PRESENTABLE_NAME) {

  companion object {
    val STRING_ID = "mpfshell"
    val PRESENTABLE_NAME = "mpfshell"
    val ID = FacetTypeId<MpfshellFacet>(STRING_ID)
    val LOGO = IconLoader.getIcon("/Logo.png")

    fun getInstance() = findInstance(MpfshellFacetType::class.java)!!
  }

  override fun createDefaultConfiguration() = MpfshellFacetConfiguration()

  override fun createFacet(module: Module, name: String, configuration: MpfshellFacetConfiguration,
                           underlyingFacet: Facet<*>?) =
      MpfshellFacet(this, module, name, configuration, underlyingFacet)

  override fun isSuitableModuleType(moduleType: ModuleType<*>?) = moduleType is PythonModuleTypeBase

  override fun getIcon(): Icon? = LOGO
}
