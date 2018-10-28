####  A simple CORS proxy

This is an example project implementing a simple CORS proxy as a FN function (http://fnproject.io) with Groovy 
using the FN Gradle plugin (https://github.com/devsoap/fn-gradle-plugin) 

To test the function perform the following steps:
   1) Start your FN server locally with ``fn start``
   2) Deploy the function by running ``./gradlew fnDeploy`` in the root folder
   3) To test the function run ``./gradlew fnInvoke --params="url=http://echo.jsontest.com/hello/world"`` 
   
The proxy will perform the request and return the result with the **Access-Control-Allow-Origin** header set to ``*`` 
so you can use the response in any of your web applications.

Please note this is example is only meant to demonstrate the capabilities of the Gradle plugin and should not be used in 
production.