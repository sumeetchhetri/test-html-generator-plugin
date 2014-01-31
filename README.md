test-html-generator-plugin
==========================

Generate Test HTML Pages for your Rest-full API's

It is a maven plugin and an executale jar file which generates HTML test pages for all your REST-ful APIs, it also generates documentation for any javadoc provided on your service methods, the plugin looks for the JAX-RS annotations on the API methods to generate test pages. <br/>
The Lifecycle phase for the plugin is the maven compile phase<br/>
Please use version 1.6 as it has the latest features and bug fixes.

Introduction
====
The test-html-generator-plugin a.k.a testgen plugin generates beautiful HTML test pages for all your restful services, it also generates mock test data samples for all Jackson/JAXB annotated beans. It generates dynamic forms breaking down your Java beans(POJO's) to the html input elements. It maps enum fields to a select(dropdown list), date fields to a datetime picker and other input fields in HTML. It also provides in-built validation on all your form fields by looking at your POJO definition(number, boolean, date etc), even complex java objects like lists and maps are easily mapped to form fields by testgen. It also provides an in-built API testing platform where you can even send custom data to your API's. The plugin also generates HTML documentation for your API's looking at the javadoc present on your service methods. It solves a lot of problems related to testing your API's during the development phase. It supports two different UI themes one based on the latest bootstrap 3 library and the other a custom developed(simple) theme which was the default theme for the previous versions of the plugin. Hope this plugin helps solve all your Rest-ful API testing woes.

Example
====
There is an example in the public domain as well showcasing the usage of this plugin and the other plugin I built [gatf][1]. The example showcases 4 example scenarios. For the source code used to generate the beautiful HTML test pages please refer the [Sample Application][2], the AuthenticationServiceImpl and the ExampleBeanServiceImpl class files are the source files used to generate the lovely HTML pages showcased here at [Testgen Pages][3].

There are 4 example scenarios as i mentioned, the API's created in the above sample app are capable of consuming/producing both JSON/XML. So the scenarios are
 1. Generate HTML pages with JSON/JSON request/response and the latest bootstrap UI enabled [JSON-bootstrap][4]
 2. Generate HTML pages with JSON/JSON request/response and the default UI enabled [JSON-default][5]
 3. Generate HTML pages with XML/XML request/response and the latest bootstrap UI enabled [XML-bootstrap][6]
 4. Generate HTML pages with XML/XML request/response and the default UI enabled [XML-default][7]

It generates a login test page called the TestLogin which can be used as an authentication step to proceed with the API work-flow. An example Login test page is [here][8]. The sample app defines 10 users which any of which can be used for testing the authentication functionality namely, user1,user2....,user10 and the password is 'password'. There are 10 predefined ExampleBean objects also defined which will be returned back whenever you invoke the GET API's on the ExampleService. Alos notice the 'Use Raw Text' functionality it may surprise you! So please look at the sample app, test it and have fun!!



Usage - Maven
====
The pom.xml configuration is as follows,

```xml
 <pluginRepositories>
	<pluginRepository>
	    <id>testgen-repository</id>
	    <name>Testgen Repository</name>
	    <url>https://raw2.github.com/sumeetchhetri/test-html-generator-plugin/master/maven/</url>
	</pluginRepository>
 </pluginRepositories>
 <plugin>
  	<groupId>com.testgen</groupId>
  	<artifactId>test-html-generator-plugin</artifactId>
  	<version>1.6</version>
  	<configuration>
		<!-- the comma separated list of classes/packages to scan for generating Test HTML pages as well as documentation from javadocs-->
  		<docTestPaths>
  			<docTestPath>com.my.restful.services.v1.</docTestPath>
  		</docTestPaths>
		
		<!-- the comma separated list of classes/packages to scan for generating documentation from javadocs-->
  		<docPaths>
  			<docPath>com.my.restful.dto.MyDTO</docPath>
  			<docPath>com.my.restful.mydtos.</docPath>
  		</docPaths>
		
		<!-- the comma separated list of classes/packages to scan for generating Test HTML pages -->
  		<testPaths>
  			<testPath>com.my.restful.services.v2.</testPath>
  		</testPaths>
  		
		<!-- Add extra links to the testgen pages on the right hand side links section-->
  		<links>
  			<link>link1.html</link>
  			<link>link2.html</link>
  		</links>
		
		<!-- The request content-type for generating mock json/xml objects which will be set in the http content body  -->
  		<requestContentType>json</requestContentType>
		
		<!-- Generate the latest boststap 3 based HTML pages/or the old styled HTML -->
  		<useBootstrapUI>true</useBootstrapUI>
		
		<!-- The URL prefix to be used while genarting URL's for test pages -->
		<urlPrefix>url/prefix</urlPrefix>
		
		<!-- The URL suffix(will be attached in the query section of the URL) to be used while genarting URL's for test pages -->
		<urlSuffix>urlsuffix</urlSuffix>
		
		<!-- The resource path be looked at for any additional html/css/html links -->
  		<resourcepath>src/main/resources</resourcepath>
		
		<!-- The location where the HTML will be generated -->
  		<uripath>testgen</uripath>
		
		<!-- The copywright to be added to your HTML pages -->
  		<copywright>Testgen 2012</copywright>
		
		<!-- The authentication URL if any -->
  		<loginpath>login</loginpath>
		
		<!-- The authentication HTTP method -->
  		<loginmeth>POST</loginmeth>
		
		<!-- The authentication token parameter name and a way to extract it from the response
		     Here it simply mentions that on successful login the token parameter will be returned
		     in the JSON body {"token": "jkfjfjkgfk123123kjgkj1"}-->
  		<authextract>token,json</authextract>
  		<!-- It will be an xpath expression in case response content type is XML -->
  		<!--
			<authextract>//node//token,xml</authextract>
  		-->
  		<!-- 
  			<authextract>token,plain</authextract>
  		-->
  		<!-- 
  			<authextract>token,header</authextract>
  		-->
		
		<!-- After authentication, how will the token be used to authenticate further requests
		     not required for cookie based authe mechanism -->
  		<apiauth>token,queryparam</apiauth>
  		<!-- 
  			<apiauth>token,postparam</apiauth>
  		-->
  		<!-- 
  			<apiauth>token,header</apiauth>
  		-->
		
		<!-- Where to send the username parameter-->
  		<loginuser>username,header</loginuser>
		
		<!-- Where to send the password parameter-->
  		<loginpass>password,header</loginpass>					
  		
		<!-- 
  			<loginuser>username,postparam</loginuser>
  			<loginpass>password,postparam</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,queryparam</loginuser>
  			<loginpass>password,queryparam</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,json</loginuser>
  			<loginpass>password,json</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,authbasic</loginuser>
  			<loginpass>password,authbasic</loginpass>
  		-->
  	</configuration>
  	<executions>
  		<execution>
  			<goals>
  				<goal>testgen</goal>
  			</goals>
  		</execution>
  	</executions>
  </plugin>
  <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-war-plugin</artifactId>
         <version>2.2</version>
         <configuration>
             <webResources>
                 <resource>
                     <directory>${project.reporting.outputDirectory}/testgen</directory>
                     <targetPath>testgen</targetPath>
                 </resource>
             </webResources>
         </configuration>
    </plugin>
```

Usage - Executable JAR
===
First define a configuration file with the following contents,
```xml
<configuration>
		<!-- the comma separated list of classes/packages to scan for generating Test HTML pages as well as documentation from javadocs-->
  		<docTestPaths>
  			<docTestPath>com.my.restful.services.v1.</docTestPath>
  		</docTestPaths>
		
		<!-- the comma separated list of classes/packages to scan for generating documentation from javadocs-->
  		<docPaths>
  			<docPath>com.my.restful.dto.MyDTO</docPath>
  			<docPath>com.my.restful.mydtos.</docPath>
  		</docPaths>
		
		<!-- the comma separated list of classes/packages to scan for generating Test HTML pages -->
  		<testPaths>
  			<testPath>com.my.restful.services.v2.</testPath>
  		</testPaths>
  		
		<!-- Add extra links to the testgen pages on the right hand side links section-->
  		<links>
  			<link>link1.html</link>
  			<link>link2.html</link>
  		</links>
		
		<!-- The request content-type for generating mock json/xml objects which will be set in the http content body  -->
  		<requestContentType>json</requestContentType>
		
		<!-- Generate the latest boststap 3 based HTML pages/or the old styled HTML -->
  		<useBootstrapUI>true</useBootstrapUI>
		
		<!-- The URL prefix to be used while genarting URL's for test pages -->
		<urlPrefix>url/prefix</urlPrefix>
		
		<!-- The URL suffix(will be attached in the query section of the URL) to be used while genarting URL's for test pages -->
		<urlSuffix>urlsuffix</urlSuffix>
		
		<!-- The resource path be looked at for any additional html/css/html links -->
  		<resourcepath>src/main/resources</resourcepath>
		
		<!-- The location where the HTML will be generated -->
  		<uripath>testgen</uripath>
		
		<!-- The copywright to be added to your HTML pages -->
  		<copywright>Testgen 2012</copywright>
		
		<!-- The authentication URL if any -->
  		<loginpath>login</loginpath>
		
		<!-- The authentication HTTP method -->
  		<loginmeth>POST</loginmeth>
		
		<!-- The authentication token parameter name and a way to extract it from the response
		     Here it simply mentions that on successful login the token parameter will be returned
		     in the JSON body {"token": "jkfjfjkgfk123123kjgkj1"}-->
  		<authextract>token,json</authextract>
  		<!-- It will be an xpath expression in case response content type is XML -->
  		<!--
			<authextract>//node//token,xml</authextract>
  		-->
  		<!-- 
  			<authextract>token,plain</authextract>
  		-->
  		<!-- 
  			<authextract>token,header</authextract>
  		-->
		
		<!-- After authentication, how will the token be used to authenticate further requests
		     not required for cookie based authe mechanism -->
  		<apiauth>token,queryparam</apiauth>
  		<!-- 
  			<apiauth>token,postparam</apiauth>
  		-->
  		<!-- 
  			<apiauth>token,header</apiauth>
  		-->
		
		<!-- Where to send the username parameter-->
  		<loginuser>username,header</loginuser>
		
		<!-- Where to send the password parameter-->
  		<loginpass>password,header</loginpass>					
  		
		<!-- 
  			<loginuser>username,postparam</loginuser>
  			<loginpass>password,postparam</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,queryparam</loginuser>
  			<loginpass>password,queryparam</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,json</loginuser>
  			<loginpass>password,json</loginpass>
  		-->
  		<!-- 
  			<loginuser>username,authbasic</loginuser>
  			<loginpass>password,authbasic</loginpass>
  		-->
</configuration>
```

You will notice that the configuration is exactly same as the maven configuration, yes you are right. Using this file you can easily execute the plugin as

user@localhost# java -jar test-html-generator-plugin-1.6.jar configuration.xml


[1]:https://github.com/sumeetchhetri/gatf
[2]:http://lit-savannah-1186.herokuapp.com/api-source/
[3]:http://lit-savannah-1186.herokuapp.com/
[4]:http://lit-savannah-1186.herokuapp.com/testgen-json1/index.html
[5]:http://lit-savannah-1186.herokuapp.com/testgen-json2/index.html
[6]:http://lit-savannah-1186.herokuapp.com/testgen-xml1/index.html
[7]:http://lit-savannah-1186.herokuapp.com/testgen-xml2/index.html
[8]:http://lit-savannah-1186.herokuapp.com/testgen-json1/TestLogin.html

License
------
Apache License Version 2.0

**Free Software, Hell Yeah!**
