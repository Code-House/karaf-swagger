# Karaf Swagger

This project provides support for running Swagger annotated services inside Karaf. Starting from Swagger 1.5 core of library is
switched to pure java which makes it easier to run, however there is still some flaws in OSGi support. One of main concerns which
is quite typical is split package problem, another is lack of proper classloader management.
To avoid troubles this project wraps all necessary swagger dependencies into Karaf feature set and allows deployment of it
via single command.

Requires
---
Karaf 3.x

Provides
---
Swagger features for running it inside karaf.

## Developer introduction

To start using swagger in your project you need to install karaf feature set:

```
feature:repo-add mvn:org.code-house.swagger/features/3.0.0-SNAPSHOT/xml
feature:install swagger
```

This will install two main parts - swagger-ui and swagger-core, which is responsible for generating composite swagger
descriptor for all running cxf jaxrs endpoints. To reach GUI you need to visit:
http://localhost:8181/swagger-ui/index.html

Swagger descriptor is available at:
http://localhost:8181/cxf/swagger/swagger.json

