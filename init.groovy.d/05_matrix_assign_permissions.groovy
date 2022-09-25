def user_name = "sample_user"

def instance = jenkins.model.Jenkins.instance
def strategy = new hudson.security.ProjectMatrixAuthorizationStrategy()

def entry = org.jenkinsci.plugins.matrixauth.PermissionEntry.user(user_name)
def permissions = [
  jenkins.model.Jenkins.READ,
  hudson.model.Item.BUILD,
  hudson.model.Item.READ,
  hudson.model.Item.CONFIGURE,
  hudson.model.Item.DELETE
]
for (permission in permissions) {
  strategy.add(permission, entry)
}

// 初期構築時は、誰もログインできなくならないように実施
if (!strategy.getGrantedPermissionEntries().containsKey(jenkins.model.Jenkins.ADMINISTER)) {
  def admin_entry = org.jenkinsci.plugins.matrixauth.PermissionEntry.user("admin")
  strategy.add(jenkins.model.Jenkins.ADMINISTER, admin_entry);
}

instance.setAuthorizationStrategy(strategy)
instance.save()
