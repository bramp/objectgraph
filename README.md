objectgraph
===========
by Andrew Brampton 2014,2016

![Java](https://img.shields.io/badge/Java-6+-brightgreen.svg)
[![Build Status](https://img.shields.io/travis/bramp/objectgraph.svg)](https://travis-ci.org/bramp/objectgraph)
[![Maven](https://img.shields.io/maven-central/v/net.bramp.objectgraph/objectgraph.svg)](http://mvnrepository.com/artifact/net.bramp.objectgraph/objectgraph)

Simple library to traverse Java object graph. A breadth-first search is conducted
on each field of each object encountered. No object is visited twice, and the
algorithm is array based, so does not encounter stack overflow issues.


Maven
-----

```xml
<dependency>
	<groupId>net.bramp.objectgraph</groupId>
	<artifactId>objectgraph</artifactId>
	<version>1.0.1</version>
</dependency>
```

Example
-------

```java
ObjectGraph
    .visitor(new ObjectGraph.Visitor() {
	    @Override
	    public boolean visit(Object object, Class clazz) {
		    System.out.println("visited " + object.toString());
		    return false;
	    }
    })
    .excludeStatic()
    .excludeTransient()
    .traverse( myObject );
```

Use Cases
---------

We were encountering an issue where Hibernate entities were accidentally being
stored in a HTTP session. This can cause a number of issues. To help alert new
developers to this issue, a Servlet filter was created that during development
would search the session for any Hibernate entities. If a entity was found, a
friendly message would be displayed to encourage the developer to fix the
problem.

This library has numerous other uses cases, however, in its current form the
API is not well suited for serialising a object graph

Build and Release
-----------------

To build this project use ``mvn``.

To push a release to maven central use the standard maven release plugin, and Sonatype's OSS repo:

```bash
mvn release:prepare
mvn release:perform
```

Licence
-------
	Copyright (c) 2014,2016, Andrew Brampton
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
	
	* Redistributions of source code must retain the above copyright notice, this
	  list of conditions and the following disclaimer.
	
	* Redistributions in binary form must reproduce the above copyright notice,
	  this list of conditions and the following disclaimer in the documentation
	  and/or other materials provided with the distribution.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
	FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
	DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
	CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
	OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
