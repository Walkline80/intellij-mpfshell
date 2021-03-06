package com.jetbrains.mpfshell.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute

/**
 * @author vlan
 */
@State(name = "MpfshellDevices", storages = arrayOf(Storage(StoragePathMacros.WORKSPACE_FILE)))
class MpfshellDevicesConfiguration : PersistentStateComponent<MpfshellDevicesConfiguration> {

  companion object {
    fun getInstance(project: Project): MpfshellDevicesConfiguration =
        ServiceManager.getService(project, MpfshellDevicesConfiguration::class.java)
  }

  // Currently the device path is stored per project, not per module
  @Attribute var devicePath: String = ""

  override fun getState() = this

  override fun loadState(state: MpfshellDevicesConfiguration) {
    XmlSerializerUtil.copyBean(state, this)
  }
}
