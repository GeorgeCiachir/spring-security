# High level project description

This is a complete setup that includes the Client, the Resource server and several Authorization servers.
The setup is simple:

The Client can be called on localhost:9091/hello, it prompts the user tp login, obtains a JWT and with this JWT then
calls the Resource server which is running on localhost:9090/hello and just returns th RS response.
The RS accepts JWT login. The RS also provides the possibility to login via a login page with multiple auth servers.

Worth mentioning that because of how the Spring OAuth2 server works, the redirect_uri cannot be localhost. We can use 127.0.0.1.
This means that for logging in with the Spring auth server, because of a session cookie that needs to be set on the redirect_uri,
we need to access both the Client or the Resource server on 127.0.0.0:9091/hello -> Client or 127.0.0.0:9090/hello -> RS

For the authorization servers, I decided to:
1. implement a Spring OAuth2 authorization server running on localhost:9092
2. configure a Keycloak server locally, running on localhost:8080
3. configure an Auth0 project at https://auth0.com/
4. configure GitHub authentication

Client login page:
![client_login_page.png](pics/client_login_page.png)

Resource server login page:
![resource_server_login_page.png](pics/resource_server_login_page.png)


# Obtain JWTs using the authorization code flow

In the [http-requests dir](http-requests) you can find some Intellij http requests flows that can be used to obtain the 
JWT from all 3 auth servers using the authorization code flow with PKCE and one http requests file to call the resource server.

# GitHub setup
https://github.com/settings/developers
![developer_page.png](pics/github/developer_page.png)
![app_settings.png](pics/github/app_settings.png)

# Keycloak setup

For the RS I have also configured keycloak to use GitHub as third party provider
![github_provider_config.png](pics/keycloak/github_provider_config.png)

![keycloak_login.png](pics/keycloak/keycloak_login.png)

![realm.png](pics/keycloak/realm.png)

## Defining a scope

![define_a_client_scope.png](pics/keycloak/define_a_client_scope.png)

![scope_settings.png](pics/keycloak/scope_settings.png)

![add_claims_mappers.png](pics/keycloak/add_claims_mappers.png)

![user_name_custom_mapper_example.png](pics/keycloak/user_name_custom_mapper_example.png)

## Defining a client

![clients.png](pics/keycloak/clients.png)

![define_a_client.png](pics/keycloak/define_a_client.png)

![dont_forget_to_add_the_allowed_redirect_uris.png](pics/keycloak/dont_forget_to_add_the_allowed_redirect_uris.png)

![add_scopes_to_the_client.png](pics/keycloak/add_scopes_to_the_client.png)


## Defining user roles

![user_roles.png](pics/keycloak/user_roles.png)

## Define users

![users.png](pics/keycloak/users.png)

![define_a_user.png](pics/keycloak/define_a_user.png)

![add_user_roles.png](pics/keycloak/add_user_roles.png)