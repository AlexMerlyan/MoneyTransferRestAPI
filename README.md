Task:
Design and implement a RESTful API (including data model and the backing implementation)
for money transfers between accounts.

How to run:
You can open project via Intellij IDEA or another IDE and run test file.
For run web server you should run main method in Server class.
I suppose you will use IDE for this, but you also could use terminal command line.
For running from terminal you need to install maven version 3.6.0 at least.

Then run commands:
mvn install <br />
java -jar target/MoneyTransferRestAPI-1.0-SNAPSHOT.jar

Stack of technologies:
java 8, maven, junit

Libraries:
org.projectlombok - for avoid boilerplate of code (getters/setters and other stuff)
com.google.code.gson - to convert java object to JSON format
junit - for testing
httpclient- for simplify creation http requests