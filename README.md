ProtectPayClient
================

The goal of this project is to create an easy to use Java client which provides
integration to ProPay's ProtectPay product.  This project uses wsimport with a
custom JAXB binding file to generate code from the ProtectPay WSDL URL. This
library depends on the Glasfish Metro library to deal with proprietary
extensions added by Microsoft in their SOAP implementation which ProPay uses.

Javadoc documentation can be found at

http://netradius.github.io/ProtectPayClient/apidocs/

This library is available in the NetRadius maven repository located at 

https://mirrors.netradius.com/nexus/content/repositories/releases

In order to use this library in your project, add the following dependency

    <dependency>
        <groupId>com.netradius.protectpay</groupId>
        <artifactId>ProtectPayClient</artifactId>
        <version>1.0.0</version>
    </dependency>

You may also need to tell Maven where the repository is. In order to do this,
you can add the following repository entry to your pom.xml file

    <repositories>
        <repository>
            <id>netradius-public-releases</id>
            <name>NetRadius Public Release Repository</name>
            <url>http://mirrors.netradius.com/nexus/content/groups/public</url>
            <releases>
                <enabled>true</enabled>
                <updatePolicy>daily</updatePolicy>
                <checksumPolicy>warn</checksumPolicy>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
