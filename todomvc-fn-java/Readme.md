# Serverless TodoMVC application with React and FN Project

This project demonstrates how to write fully serverless web applications with Fn Project and React. This project is a 
multimodule project with two functions; UI and API.


This is an example project for the blog article https://devsoap.com/serverless-apps-with-react.
A running demo application can be found at https://fns.devsoap.com/t/todomvc

To run this project you will need valid AWS DynamoDB credentials where to todo items will persisted.

To run the project locally run the following on a terminal:
```bash
> ./gradlew fnStart
> ./gradlew fnDeploy -Paws.accessKeyId=<access key> -Paws.secretKey=<secret key> -Paws.region=<region>
```

Once the application has successfully been deployed, it can be accessed via http://localhost:8080/t/todomvc.
