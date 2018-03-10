Hammock
=======

[![Gitter chat](https://badges.gitter.im/hammock-project/gitter.png)](https://gitter.im/hammock-project/Lobby)
[![Build Status](https://travis-ci.org/hammock-project/hammock.png)](https://travis-ci.org/hammock-project/hammock)
[![Maven Central](https://img.shields.io/maven-central/v/ws.ament.hammock/hammock-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cws.ament.hammock)

![Hammock](https://hammock-project.github.io/img/src/Large.png)

_Building Microservices so easily you're laying in a Hammock!_

Hammock is a simple to use framework for bootstrapping CDI, launching a web server and being able to deploy REST APIs.  It takes a best practice approach to creating a runtime for you, so that you can focus on the important stuff.

Read through some of the basics to get started, or view the [wiki](https://github.com/hammock-project/hammock/wiki)

## Getting Started

First, add some dependencies to your project.  Easiest way to start is with a Micrprofile Distribution - Standard or Cochise

### Using Maven

Containers bring in transitive dependencies to bring up your runtime.

#### Using Undertow, Weld, Apache Johnzon, CXF

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>dist-microprofile</artifactId>
    <version>2.1</version>
</dependency>
```

#### Using Apache Tomcat, OpenWebBeans, Johnzon, CXF

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>dist-microprofile-cochise</artifactId>
    <version>2.1</version>
</dependency>
```

### Launching your first app

Now that you have your dependencies in order, you can launch your first app.  

#### Using Hammock's Bootstrap

Hammock has a bootstrap class, `ws.ament.hammock.Bootstrap` which will start CDI for you.  It uses implementations of `Bootstrapper` to start the appropriate container.

#### Using Your Container's Bootstrap

You can also bootstrap your container directly.  Since Hammock is implemented as a suite of CDI components, no extra work is required.

#### Use your own Bootstrap

You may want your own bootstrap, to do some pre-flight checks.  Just make sure you either initialize Hammock or Weld when you're done.  Your main might look as simple as

```java
public class CustomBootstrapper implements Bootstrapper {

    public void start() {
        new PreflightChecks().verify();
        new Weld().initialize();
    }

    public void stop() { }
}
```

Make sure you add this to the `ServiceLoader`

#### Executable JARs

You'll likely want to create an executable JAR.  Just shade in all dependencies using your favorite build tool, and set the main class to your choice of main class.  I recommend using [Capsule](http://www.capsule.io/)

### Configuration

Configuration is provided via Apache DeltaSpike.  The default configuration uses port 8080 for your webserver and /tmp for your static file directory.  You'll likely want to configure those for your project.

### Security

Basic security support is available.  Two CDI interceptors are in use, one for verifying a user is logged in and another for verifying roles.  To make use of security, you'll need to implement the `Identity` interface and make it a bean to represent the user currently be acted upon, usually of `RequestScope`.  
* `@LoggedIn` annotate a class or method, and an interceptor will check that the user is logged in for this method invocation.
* `@HasAllRoles()` annotate a class or method, and an interceptor will check that the current identity has all of the roles defined.

To add the security runtime to your app, just include this dependency.

```xml
<dependency>
    <groupId>ws.ament.hammock</groupId>
    <artifactId>security-spi</artifactId>
    <version>2.1</version>
</dependency>
```

### Issue Tracking

Have a feature request? Or found an issue? Please use [github issues](https://github.com/hammock-project/hammock/issues) to let us know!