Words Indexing - recruitment task

Requirements:
Write a Java program, which will index each letter in a text to words. 
Program does not have to take letter case into consideration. 
Words that occur many times in a text must be presented only once in output. 
List of words in which particular letter occurs, should be sorted alphabetically.

Input text: ala ma kota, kot koduje w Javie Kota – (eng. Ala has a cat, a cat develops a cat in Java ;) )

Output:
a: ala, javie, kota, ma
d: koduje
e: javie, koduje
i: javie
j: javie, koduje
k: koduje, kot, kota
l: ala
m: ma
o: koduje, kot, kota
t: kot, kota
u: koduje
v: javie
w: w


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