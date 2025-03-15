Before opening the project, ensure the following requirements are met:

Maven is installed.
Java 21 is installed.
Docker is installed.
To run the program, follow these steps:

After downloading or cloning the project (and extracting it if necessary),

First, open a terminal or command line in the project directory. For example:

"C:\Users\exampleUser\Desktop\calculatorapi\calculatorapi"

Then, run the following commands:

"mvn clean install" followed by "docker-compose up --build" .

After the process is complete, open another terminal and test the REST API.

For example:

curl "http://localhost:8080/api/divide?a=2&b=226"