## Define a tenant

![define_tenant.png](pics/auth0/define_tenant.png)

## Define an API (this is the resource server representation)

![define_api.png](pics/auth0/define_api.png)

![api_definition.png](pics/auth0/api_definition.png)

![define_api_permissions.png](pics/auth0/define_api_permissions.png)

## Define applications that will access the api (these are the clients of the api)
Note that for the resource server to be able to offer login capabilities, it also needs to be registered here as 
a client of the api.

![define_applications.png](pics/auth0/define_applications.png)

![application_definition.png](pics/auth0/application_definition.png)

![dont_forget_to_add_the_allowed_redirect_uris.png](pics/auth0/dont_forget_to_add_the_allowed_redirect_uris.png)

## Create roles for the user

![roles.png](pics/auth0/roles.png)

![role_definition.png](pics/auth0/role_definition.png)

![can_add_permissions.png](pics/auth0/can_add_permissions.png)

![add_user_to_the_role.png](pics/auth0/add_user_to_the_role.png)

## Create users

![users.png](pics/auth0/users.png)

![add_roles.png](pics/auth0/add_roles.png)

![assign_permissions_on_apis.png](pics/auth0/assign_permissions_on_apis.png)

## Add additional claims
You can add more claims to the token (similar to the keycloak token mappers)

![create_action_do_add_claims_on_login_trigger.png](pics/auth0/create_action_do_add_claims_on_login_trigger.png)

![create_and_deploy_action.png](pics/auth0/create_and_deploy_action.png)