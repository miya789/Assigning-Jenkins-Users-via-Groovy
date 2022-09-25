def user_name = "sample_user"
def pass = "password"

def instance = jenkins.model.Jenkins.instance
def realm = instance.securityRealm
realm.createAccount(user_name, pass)

instance.save()
