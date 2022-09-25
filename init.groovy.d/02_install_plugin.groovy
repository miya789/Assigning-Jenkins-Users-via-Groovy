def plugins   = ["role-strategy"]
// def plugins   = ["matrix-auth"]

def instance  = jenkins.model.Jenkins.getInstance()
pm = instance.getPluginManager()
uc = instance.getUpdateCenter()
uc.updateAllSites()

def enablePlugin(pluginName) {
  if (! pm.getPlugin(pluginName)) {
    deployment = uc.getPlugin(pluginName).deploy(true)
    deployment.get()
  }

  def plugin = pm.getPlugin(pluginName)
  if (! plugin.isEnabled()) {
    plugin.enable()
  }

  plugin.getDependencies().each {
    enablePlugin(it.shortName)
  }
}

plugins.each {
  enablePlugin(it)
}
