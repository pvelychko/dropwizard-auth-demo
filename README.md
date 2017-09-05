# Dropwizard-auth-demo

## Developer task
Create an HTTP-based server API based on persistent storage (perhaps a database). You should also create a client consuming the API, demonstrating that the solution satisfies the below requirements.

### Minimum requirements
The application should allow a user to:
Register
Authenticate
Access a list of timestamps showing the user's last 5 successful login attempts. The user should only be able to see it’s own list

### Implementation details
A Dropwizard application than uses Mustache View templates, both basic and token-based auth and a cloud PostgreSQL database.‎

## How to run it

Run to build the application
```
mvn clean install
```

Start application with
```
java -jar target/izettle-test-assignment.jar server server-config.yml
```

To check that your application is running enter url `http://localhost:8080`