Words Indexing
Application for indexing words from given user text.
It is Command line application interacts with user by the terminal/console.

Check settings of the encoding type in your system terminal/console.
You should use UTF-8 standard. 
Set up encoding type using command:
Windows: chcp 1250
Linux:   localectl set-locale LANG=en_US.utf8

Build project: 
mvn clean package

Run application:
Directly from project: mvn spring-boot:run
Using java jdk:        java -jar wordsindex-1.0-SNAPSHOT.jar