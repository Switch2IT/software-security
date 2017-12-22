# software-security
[ ![Codeship Status for Switch2IT/software-security](https://app.codeship.com/projects/22e31740-c81d-0135-5fef-4a15d2e65107/status?branch=master)](https://app.codeship.com/projects/261519)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c12f86302321478496e175bcee39eb6f)](https://www.codacy.com/app/Switch2IT/software-security?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Switch2IT/software-security&amp;utm_campaign=Badge_Grade)

A Java EE REST API providing functionalities required by the Erasmushogeschool Software Security course.
Makes use of Java8, Jose4J, JaxRs, Typesafe Config, Swagger, Wildfly Webapp Maven archetype.
Developed by [Guillaume Vandecasteele](mailto:guillaume.vandecasteele@student.ehb.be).

## How to use

This project contains a small demo login page. If you wish to make use of it, paste a `keycloak.json` file in the `/src/main/webapp/client` folder before building. To try it out, navigate to `/software-security/client`

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

This repository also contains a small util to create a X.509 certificate and private key (in `PEM` format) to use in your choice of OpenID providers. Simply run the main method in the PkiUtil class and follow the prompts in the console.
