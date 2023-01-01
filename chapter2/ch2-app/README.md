# First simple app

When spring-security is added, basic auth is automatically added and all endpoints get secured.
- Grab the password from the logs -> the UUID
- The user is -> user

Then
curl -u user:psw http://localhost:8080/hello

Or run the [hello-controller.http](http-requests/hello-controller.http) requests with the [http-client.env.json](http-requests/http-client.env.json)

![main_components.png](../pics/main_components.png)


## Setting up https

1. Generate a self signed certificate
    `winpty openssl req -newkey rsa:2048 -x509 -keyout key.pem -out cert.pem -days 365`

2. Continue with signing the certificate
    `winpty openssl pkcs12 -export -in cert.pem -inkey key.pem -out certificate.p12 -name "certificate"`