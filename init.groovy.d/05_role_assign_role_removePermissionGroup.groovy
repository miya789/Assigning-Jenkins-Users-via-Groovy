def user_name = "sample_user"
def role_name = "sample_role"

def instance = jenkins.model.Jenkins.instance
def strategy = new com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy()
def globalRoleMap = strategy.getRoleMap(com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType.Global)

def _permissions = new HashSet<>()
_permissions.add(jenkins.model.Jenkins.READ) // これは必ず必要

def groups = new ArrayList<>(hudson.security.PermissionGroup.getAll())
groups.remove(hudson.security.PermissionGroup.get(hudson.security.Permission.class))
groups.remove(hudson.security.PermissionGroup.get(hudson.model.Hudson.class))
_permissions.addAll(new HashSet<>(groups.stream().map{g -> g.getPermissions()}.collect().flatten()))
permissions = new HashSet<>(_permissions.stream().filter{ it.enabled }.collect()) // enableでないPermissionを除去

def role = new com.michelin.cio.hudson.plugins.rolestrategy.Role(role_name, permissions)
globalRoleMap.addRole(role)
globalRoleMap.assignRole(role, user_name)

// 初期構築時は、誰もログインできなくならないように実施
if (! globalRoleMap.getRoles().stream().anyMatch{p -> p.hasPermission(jenkins.model.Jenkins.ADMINISTER)}) {
  def adminRole = new com.michelin.cio.hudson.plugins.rolestrategy.Role("admin", new HashSet(Arrays.asList(jenkins.model.Jenkins.ADMINISTER)))

  globalRoleMap.addRole(adminRole)
  globalRoleMap.assignRole(adminRole, "admin")
}

instance.setAuthorizationStrategy(strategy)
instance.save()
