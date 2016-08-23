Hammock
=======

[![Build Status](https://travis-ci.org/hammock-project/hammock.png)](https://travis-ci.org/hammock-project/hammock)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ws.ament.hammock/hammock/badge.png?style=flat)](http://search.maven.org/#search%7Cga%7C1%7Cws.ament.hammock)
[![Dependency Status](https://www.versioneye.com/user/projects/574cdc006497d90039ac460e/badge.svg?style=flat)](https://www.versioneye.com/user/projects/574cdc006497d90039ac460e)

_Building Microservices so easily you're laying in a Hammock!_

Hammock is a simple to use framework for bootstrapping CDI, launching a web server and being able to deploy REST APIs.  It takes a best practice approach to creating a runtime for you, so that you can focus on the important stuff.

Read through some of the basics to get started, or view the [wiki](https://github.com/hammock-project/hammock/wiki)

## Getting Started

First, add some dependencies to your project.  You can choose between Undertow and Jetty presently

### Using Maven

Both containers bring in transitive dependencies to bring up your runtime.

#### Using Undertow

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>web-undertow</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Using Jetty

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>web-jetty</artifactId>
    <version>0.1.0</version>
</dependency>
```

Next, you'll want to add a REST Runtime to your application.  The default is to use RestEasy.

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>rest-resteasy</artifactId>
    <version>0.1.0</version>
</dependency>
```

But you can also use CXF

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>rest-resteasy</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Launching your first app

Now that you have your dependencies in order, you can launch your first app.  

#### Using Hammock's Bootstrap

Hammock has a bootstrap class, `ws.ament.hammock.Bootstrap` which will start Weld for you.  It just wraps a call to Weld's boostrap

#### Using Weld's Bootstrap

You can also bootstrap Weld directly.  Since Hammock is implemented as a suite of CDI components, no extra work is required.

#### Use your own Bootstrap

You may want your own bootstrap, to do some pre-flight checks.  Just make sure you either initialize Hammock or Weld when you're done.  Your main might look as simple as

```java
public class Bootstrap {
    public static void main(String[] args) {
        new PreflightChecks().verify();
        new Weld().initialize();
    }
}
```

#### Executable JARs

You'll likely want to create an executable JAR.  Just shade in all dependencies using your favorite build tool, and set the main class to your choice of main class.

### Configuration

Configuration is provided via Apache Tamaya.  The default configuration uses port 8080 for your webserver and /tmp for your static file directory.  You'll likely want to configure those for your project.

### Security

Basic security support is available.  Two CDI interceptors are in use, one for verifying a user is logged in and another for verifying roles.  To make use of security, you'll need to implement the `Identity` interface and make it a bean to represent the user currently be acted upon, usually of `RequestScope`.  
* `@LoggedIn` annotate a class or method, and an interceptor will check that the user is logged in for this method invocation.
* `@HasAllRoles()` annotate a class or method, and an interceptor will check that the current identity has all of the roles defined.

To add the security runtime to your app, just include this dependency.

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>security-spi</artifactId>
    <version>0.1.0</version>
</dependency>
```

