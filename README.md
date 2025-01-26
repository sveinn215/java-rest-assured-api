## 1. Requirements

To run the tests, you need to have the following installed:

1. [Java](https://www.oracle.com/id/java/technologies/downloads/) (1.8 or higher)
2. [Maven](https://maven.apache.org/download.cgi) (3.8.9 or higher)

## 2. Setup Environment Variables

1. [Java] (https://www.geeksforgeeks.org/setting-environment-java/)
2. [Maven] https://www.tutorialspoint.com/maven/maven_environment_setup.htm

## 3. Create .env on src/test/java/com/example/apitest/resources

BEARER_TOKEN=Bearer 903b90c7b69cd1451e9769080145230cc97b6334b40cbdc7011c3f5057c0e531 
API_URL=https://gorest.co.in/public/v2/

## 4. Getting Started

To run from project root directory in Linux/MacOS terminal:

mvn clean test