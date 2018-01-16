# software-security
[ ![Codeship Status for Switch2IT/software-security](https://app.codeship.com/projects/22e31740-c81d-0135-5fef-4a15d2e65107/status?branch=master)](https://app.codeship.com/projects/261519)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c12f86302321478496e175bcee39eb6f)](https://www.codacy.com/app/Switch2IT/software-security?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Switch2IT/software-security&amp;utm_campaign=Badge_Grade)

A Java EE REST API providing functionalities required by the Erasmushogeschool Software Security course.
Makes use of Java8, Jose4J, JaxRs, Typesafe Config, Swagger, Wildfly Webapp Maven archetype.
Developed by [Guillaume Vandecasteele](mailto:guillaume.vandecasteele@student.ehb.be).

## Build Instructions



Build with maven 

```bash
mvn clean install
```

The private part of the API can be secured through Auth0, Keycloak or both. For this, the API requires a configuration file to be present at `/usr/local/software-security/application.conf` containing the following information:

This is just an example, fill in with your own values.

```js
software-security {
  idp {
    auth0 {
      audience: "https://www.vandecasteele.tk",
      issuer: "https://guycastle.eu.auth0.com/",
      jwks-uri: "https://guycastle.eu.auth0.com/.well-known/jwks.json"
    },
    keycloak {
      audience: "confidential-client",
      issuer: "https://idp.vandecasteele.tk/auth/realms/softwaresecurity",
      jwks-uri: "https://idp.vandecasteele.tk/auth/realms/softwaresecurity/protocol/openid-connect/certs"
    }
  }
}
```

Deploy on your favorite Java application server. It was developed and tested on a Wildfly 10 application server, and we make no guarantee as to its functionality on others.

## Included Web Applications

### Demo Keycloak Login Application (`/software-security/client`)

This project contains a small demo login page which can be found at `/software-security/client`. In order for it to work you **must** build this project with a valid Keycloak JSON OIDC installation file named `keycloak.json` (which you can obtain from your Keycloak client's `installation` tab) present in the `/src/main/webapp/client` folder. The file should look a little like this:

```json
{
  "realm": "softwaresecurity",
  "auth-server-url": "https://your.domain/auth",
  "ssl-required": "external",
  "resource": "client_id",
  "credentials": {
    "secret": "client_secret"
  },
  "confidential-port": 0,
  "policy-enforcer": {}
}
```

To try it out, navigate to `/software-security/client`, you can query the API private endpoint by clicking on `Show Domain Info`; this will display the domain name of the server that is hosting your application, prefixed by the current date and time. If unauthorized, it will return an `Unauthorized` message also prefixed by the current date and time. Press `Log in` or `Log out` to try querying the API in a different auth state

It is also possible to try out your token by using Postman or CURL:

```bash
curl -X GET \
  https://your.domain/software-security/v1/api/private \
  -H 'Authorization: Bearer eyJhbG...LpxbUg'
```

### Swagger UI

This project also contains a Swagger UI component, which can be found at `software-security/swagger`. The UI is built up with the `/software-security/v1/swagger.json` file that is generated based on the Java REST API's Swagger annotations.

## Utils

This repository also contains a small util to create a X.509 certificate with all Key Usage extensions and a private key (in `PEM` format) to use in your choice of OpenID providers. Simply run the main method in the PkiUtil class and follow the prompts in the console.
