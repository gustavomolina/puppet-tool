Puppet Client (Java)
This project implements a standalone client that simulates a basic Puppet-like system. It periodically polls a server for configuration changes and updates local files accordingly.

Prerequisites
Java 17 or higher
Maven
Building and Running
Clone or download the project.

Navigate to the project directory.

Run the following command to build the project:

Bash
mvn clean install
Use o código com cuidado.
Run the client with the master server URL as an argument:

Bash
java -jar puppet-client.jar <master_url>
Replace <master_url> with the actual URL of your server endpoint that provides the configuration in JSON format.

Example:

Bash
java -jar puppet-client.jar http://localhost:8080
This will start the client, which will poll the server every 5 minutes for configuration updates. If the server responds with a successful status code (200) and valid JSON data, the client will attempt to download and update any files specified in the configuration.

Additional Notes:

The server implementation and configuration file format are not included in this project.
This is a simplified example and might require adjustments for specific use cases.
Disclaimer: This project is for educational purposes only and may not be suitable for production use. Refer to the official Puppet documentation for production-ready implementations.
