Task:<br />
Design and implement a RESTful API (including data model and the backing implementation)<br />
for money transfers between accounts.<br />

How to run:<br />
You can open project via Intellij IDEA or another IDE and run test file.<br />
For run web server you should run main method in Server class.<br />
I suppose you will use IDE for this, but you also could use terminal command line.<br />
For running from terminal you need to install maven version 3.6.0 at least.<br />

Then run commands:<br />
mvn install <br />
java -jar target/MoneyTransferRestAPI-1.0-SNAPSHOT.jar<br />

Stack of technologies:<br />
java 8, maven, junit<br />

Libraries:<br />
org.projectlombok - for avoid boilerplate of code (getters/setters and other stuff)<br />
com.google.code.gson - to convert java object to JSON format<br />
junit - for testing<br />
httpclient- for simplify creation http requests<br />